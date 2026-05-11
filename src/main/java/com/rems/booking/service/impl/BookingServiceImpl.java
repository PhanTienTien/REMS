package com.rems.booking.service.impl;

import com.rems.activitylog.dao.impl.ActivityLogDAOImpl;
import com.rems.activitylog.service.ActivityLogService;
import com.rems.activitylog.service.impl.ActivityLogServiceImpl;
import com.rems.booking.dao.BookingDAO;
import com.rems.booking.dao.impl.BookingDAOImpl;
import com.rems.booking.dto.BookingAdminDetailDTO;
import com.rems.booking.dto.BookingAdminViewDTO;
import com.rems.booking.dto.CustomerBookingDTO;
import com.rems.booking.model.Booking;
import com.rems.booking.service.BookingService;
import com.rems.common.constant.ActivityLogAction;
import com.rems.common.constant.BookingStatus;
import com.rems.common.constant.PropertyStatus;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.transaction.TransactionManager;
import com.rems.common.util.PageResult;
import com.rems.property.dao.PropertyDAO;
import com.rems.property.dao.impl.PropertyDAOImpl;
import com.rems.property.model.Property;
import com.rems.property.service.PropertyService;
import com.rems.transaction.service.TransactionService;
import com.rems.transaction.service.impl.TransactionServiceImpl;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO;
    private final PropertyDAO propertyDAO;
    private final TransactionService transactionService;
    private final ActivityLogService activityLogService;
    private final TransactionManager tx;

    public BookingServiceImpl() {
        this(
                new BookingDAOImpl(),
                new PropertyDAOImpl(),
                new TransactionServiceImpl(),
                new ActivityLogServiceImpl(new TransactionManager(), new ActivityLogDAOImpl()),
                new TransactionManager()
        );
    }

    public BookingServiceImpl(PropertyService propertyService,
                              TransactionService transactionService) {
        this(
                new BookingDAOImpl(),
                new PropertyDAOImpl(),
                transactionService,
                new ActivityLogServiceImpl(new TransactionManager(), new ActivityLogDAOImpl()),
                new TransactionManager()
        );
    }

    public BookingServiceImpl(BookingDAO bookingDAO,
                              PropertyDAO propertyDAO,
                              TransactionService transactionService,
                              ActivityLogService activityLogService,
                              TransactionManager tx) {
        this.bookingDAO = bookingDAO;
        this.propertyDAO = propertyDAO;
        this.transactionService = transactionService;
        this.activityLogService = activityLogService;
        this.tx = tx;
    }

    @Override
    public Long createBooking(Long propertyId, Long customerId, String note, LocalDateTime scheduledAt) {

        return tx.execute(conn -> {
            Property property = propertyDAO.findByIdForUpdate(conn, propertyId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.PROPERTY_NOT_FOUND));

            if (property.getStatus() != PropertyStatus.AVAILABLE) {
                throw new BusinessException(ErrorCode.PROPERTY_NOT_AVAILABLE);
            }

            if (bookingDAO.existsActiveBooking(conn, propertyId, customerId)) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            Booking booking = new Booking();
            booking.setPropertyId(propertyId);
            booking.setCustomerId(customerId);
            booking.setNote(note);
            booking.setScheduledAt(scheduledAt);
            booking.setStatus(BookingStatus.PENDING);

            Long bookingId = bookingDAO.insert(conn, booking);
            log(conn, customerId, ActivityLogAction.CREATE_BOOKING, bookingId, propertyId);
            return bookingId;
        });
    }

    @Override
    public void acceptBooking(Long bookingId, Long staffId) {

        tx.executeWithoutResult(conn -> {
            Booking booking = bookingDAO.findByIdForUpdate(conn, bookingId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));

            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            Property property = propertyDAO.findByIdForUpdate(conn, booking.getPropertyId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PROPERTY_NOT_FOUND));

            if (property.getStatus() != PropertyStatus.AVAILABLE) {
                throw new BusinessException(ErrorCode.PROPERTY_NOT_AVAILABLE);
            }

            propertyDAO.updateStatus(conn, property.getId(), PropertyStatus.RESERVED);
            bookingDAO.updateStatus(conn, bookingId, BookingStatus.ACCEPTED, staffId);
            transactionService.createTransaction(conn, bookingId, staffId, "SYSTEM");
            bookingDAO.rejectOtherBookings(conn, property.getId(), bookingId, staffId);
            log(conn, staffId, ActivityLogAction.ACCEPT_BOOKING, bookingId, property.getId());
        });
    }

    @Override
    public void acceptBookingByStaff(Long bookingId, Long staffId) {

        tx.executeWithoutResult(conn -> {
            Booking booking = bookingDAO.findByIdForUpdate(conn, bookingId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));

            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            Property property = propertyDAO.findByIdForUpdate(conn, booking.getPropertyId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PROPERTY_NOT_FOUND));

            // Allow the creator of the property (Admin or Staff) to accept the booking
            if (!staffId.equals(property.getCreatedBy())) {
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }

            if (property.getStatus() != PropertyStatus.AVAILABLE) {
                throw new BusinessException(ErrorCode.PROPERTY_NOT_AVAILABLE);
            }

            propertyDAO.updateStatus(conn, property.getId(), PropertyStatus.RESERVED);
            bookingDAO.updateStatus(conn, bookingId, BookingStatus.ACCEPTED, staffId);
            transactionService.createTransaction(conn, bookingId, staffId, "SYSTEM");
            bookingDAO.rejectOtherBookings(conn, property.getId(), bookingId, staffId);
            log(conn, staffId, ActivityLogAction.ACCEPT_BOOKING, bookingId, property.getId());
        });
    }

    @Override
    public void rejectBooking(Long bookingId, Long staffId) {

        tx.executeWithoutResult(conn -> {
            Booking booking = bookingDAO.findByIdForUpdate(conn, bookingId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));

            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            bookingDAO.updateStatus(conn, bookingId, BookingStatus.REJECTED, staffId);
            log(conn, staffId, ActivityLogAction.REJECT_BOOKING, bookingId, bookingId);
        });
    }

    @Override
    public void rejectBookingByStaff(Long bookingId, Long staffId) {

        tx.executeWithoutResult(conn -> {
            Booking booking = bookingDAO.findByIdForUpdate(conn, bookingId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));

            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            Property property = propertyDAO.findByIdForUpdate(conn, booking.getPropertyId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PROPERTY_NOT_FOUND));

            // Allow the creator of the property (Admin or Staff) to reject the booking
            if (!staffId.equals(property.getCreatedBy())) {
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }

            bookingDAO.updateStatus(conn, bookingId, BookingStatus.REJECTED, staffId);
            log(conn, staffId, ActivityLogAction.REJECT_BOOKING, bookingId, bookingId);
        });
    }

    @Override
    public void cancelBooking(Long bookingId, Long customerId) {

        tx.executeWithoutResult(conn -> {
            Booking booking = bookingDAO.findByIdForUpdate(conn, bookingId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));

            if (!booking.getCustomerId().equals(customerId)) {
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }

            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            bookingDAO.updateStatus(conn, bookingId, BookingStatus.CANCELLED, null);
            log(conn, customerId, ActivityLogAction.CANCEL_BOOKING, bookingId, bookingId);
        });
    }

    @Override
    public Optional<BookingAdminDetailDTO> getBookingDetail(Long bookingId) {
        return tx.execute(conn -> bookingDAO.findDetailForAdmin(conn, bookingId));
    }

    @Override
    public Optional<BookingAdminDetailDTO> getBookingDetailForStaff(Long bookingId, Long staffId) {
        return tx.execute(conn -> bookingDAO.findDetailForStaff(conn, bookingId, staffId));
    }

    @Override
    public List<CustomerBookingDTO> getBookingsByCustomer(Long customerId) {
        return tx.execute(conn -> bookingDAO.findByCustomer(conn, customerId));
    }

    @Override
    public PageResult<CustomerBookingDTO> getBookingsByCustomerPage(Long customerId, int page, int size) {
        return tx.execute(conn -> {
            int offset = (page - 1) * size;
            List<CustomerBookingDTO> bookings = bookingDAO.findByCustomerPage(conn, customerId, size, offset);
            int total = bookingDAO.countByCustomer(conn, customerId);
            return new PageResult<>(bookings, page, size, total);
        });
    }

    @Override
    public void expirePendingBookings() {

        tx.execute(conn -> {
            List<Booking> expired = bookingDAO.findExpiredPending(conn, 24);

            for (Booking booking : expired) {
                bookingDAO.updateStatus(conn, booking.getId(), BookingStatus.CANCELLED, null);
            }

            return null;
        });
    }

    @Override
    public PageResult<BookingAdminViewDTO> searchBookings(String keyword,
                                                          BookingStatus status,
                                                          String sort,
                                                          int page,
                                                          int size) {

        return tx.execute(conn -> {
            int offset = (page - 1) * size;
            List<BookingAdminViewDTO> data = bookingDAO.search(conn, keyword, status, sort, size, offset);
            int total = bookingDAO.countSearch(conn, keyword, status);
            return new PageResult<>(data, page, size, total);
        });
    }

    @Override
    public PageResult<BookingAdminViewDTO> searchBookingsByStaff(Long staffId,
                                                                 String keyword,
                                                                 BookingStatus status,
                                                                 String sort,
                                                                 int page,
                                                                 int size) {

        return tx.execute(conn -> {
            int offset = (page - 1) * size;
            List<BookingAdminViewDTO> data = bookingDAO.searchByStaff(conn, staffId, keyword, status, sort, size, offset);
            int total = bookingDAO.countSearchByStaff(conn, staffId, keyword, status);
            return new PageResult<>(data, page, size, total);
        });
    }

    @Override
    public boolean hasPendingBooking(Long propertyId) {
        return tx.execute(conn -> bookingDAO.existsPendingByProperty(conn, propertyId));
    }

    private void log(Connection conn,
                     Long userId,
                     ActivityLogAction action,
                     Long bookingId,
                     Object... args) {
        activityLogService.log(conn, userId, action, bookingId, null, args);
    }
}
