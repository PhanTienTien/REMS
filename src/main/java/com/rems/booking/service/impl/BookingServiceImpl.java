package com.rems.booking.service.impl;

import com.rems.booking.dao.BookingDAO;
import com.rems.booking.dao.impl.BookingDAOImpl;
import com.rems.booking.dto.BookingAdminDetailDTO;
import com.rems.booking.dto.BookingAdminViewDTO;
import com.rems.booking.dto.CustomerBookingDTO;
import com.rems.booking.model.Booking;
import com.rems.booking.service.BookingService;
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

import java.util.List;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO;
    private final PropertyDAO propertyDAO;
    private final TransactionService transactionService;
    private final TransactionManager tx;

    public BookingServiceImpl() {
        this(
                new BookingDAOImpl(),
                new PropertyDAOImpl(),
                new TransactionServiceImpl(),
                new TransactionManager()
        );
    }

    public BookingServiceImpl(PropertyService propertyService,
                              TransactionService transactionService) {
        this(
                new BookingDAOImpl(),
                new PropertyDAOImpl(),
                transactionService,
                new TransactionManager()
        );
    }

    public BookingServiceImpl(BookingDAO bookingDAO,
                              PropertyDAO propertyDAO,
                              TransactionService transactionService,
                              TransactionManager tx) {
        this.bookingDAO = bookingDAO;
        this.propertyDAO = propertyDAO;
        this.transactionService = transactionService;
        this.tx = tx;
    }

    @Override
    public Long createBooking(Long propertyId, Long customerId, String note) {

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
            booking.setStatus(BookingStatus.PENDING);

            return bookingDAO.insert(conn, booking);
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
        });
    }

    @Override
    public Optional<BookingAdminDetailDTO> getBookingDetail(Long bookingId) {
        return tx.execute(conn -> bookingDAO.findDetailForAdmin(conn, bookingId));
    }

    @Override
    public List<CustomerBookingDTO> getBookingsByCustomer(Long customerId) {
        return tx.execute(conn -> bookingDAO.findByCustomer(conn, customerId));
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
}
