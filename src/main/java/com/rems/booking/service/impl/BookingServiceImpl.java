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
import com.rems.common.transaction.TransactionManager;
import com.rems.property.model.Property;
import com.rems.property.service.PropertyService;
import com.rems.transaction.service.TransactionService;

import java.util.List;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO = new BookingDAOImpl();
    private final TransactionManager tx = new TransactionManager();
    private final PropertyService propertyService;
    private final TransactionService transactionService;

    public BookingServiceImpl(PropertyService propertyService, TransactionService transactionService) {
        this.propertyService = propertyService;
        this.transactionService = transactionService;
    }

    @Override
    public Long createBooking(Long propertyId,
                              Long customerId,
                              String note) {

        return tx.execute(conn -> {

            Property property =
                    propertyService.findById(propertyId)
                            .orElseThrow(() ->
                                    new RuntimeException("Property not found"));

            if (property.getStatus() != PropertyStatus.AVAILABLE) {
                throw new RuntimeException("Property not available");
            }

            boolean exists =
                    bookingDAO.existsActiveBooking(conn,
                            propertyId,
                            customerId);

            if (exists) {
                throw new RuntimeException(
                        "You already have a booking for this property");
            }

            Booking booking = new Booking();

            booking.setPropertyId(propertyId);
            booking.setCustomerId(customerId);
            booking.setNote(note == null ? "" : note.trim());

            return bookingDAO.insert(conn, booking);
        });
    }

    @Override
    public void acceptBooking(Long bookingId, Long staffId) {

        tx.executeWithoutResult(conn -> {

            Booking booking = bookingDAO
                    .findByIdForUpdate(conn, bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new RuntimeException("Booking not pending");
            }

            Long propertyId = booking.getPropertyId();

            if (bookingDAO.existsAcceptedByProperty(conn, propertyId)) {
                throw new RuntimeException("Property already has accepted booking");
            }

            bookingDAO.updateStatus(conn,
                    bookingId,
                    BookingStatus.ACCEPTED,
                    staffId);

            propertyService.reserveProperty(propertyId, conn);
            transactionService.createTransaction(
                    conn,
                    bookingId,
                    staffId,
                    "SYSTEM"
            );

            List<Booking> pending =
                    bookingDAO.findPendingByPropertyForUpdate(conn, propertyId);

            for (Booking b : pending) {

                if (!b.getId().equals(bookingId)) {

                    bookingDAO.updateStatus(conn,
                            b.getId(),
                            BookingStatus.REJECTED,
                            staffId);
                }
            }
        });
    }

    @Override
    public void rejectBooking(Long bookingId, Long staffId) {

        tx.executeWithoutResult(conn -> {

            Booking booking = bookingDAO
                    .findByIdForUpdate(conn, bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new RuntimeException("Cannot reject");
            }

            bookingDAO.updateStatus(conn,
                    bookingId,
                    BookingStatus.REJECTED,
                    staffId);
        });
    }

    @Override
    public void cancelBooking(Long bookingId,
                              Long customerId) {

        tx.execute(conn -> {

            var bookingOpt =
                    bookingDAO.findByIdForUpdate(conn, bookingId);

            if (bookingOpt.isEmpty()) {
                throw new RuntimeException("Booking not found");
            }

            Booking booking = bookingOpt.get();

            if (!booking.getCustomerId().equals(customerId)) {
                throw new RuntimeException("Not your booking");
            }

            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new RuntimeException("Booking cannot be cancelled");
            }

            bookingDAO.updateStatus(
                    conn,
                    bookingId,
                    BookingStatus.CANCELLED,
                    null
            );

            var property =
                    propertyService
                            .findById(booking.getPropertyId())
                            .orElseThrow();

            if (property.getStatus() == PropertyStatus.RESERVED) {

                propertyService.failTransaction(
                        booking.getPropertyId(),
                        conn
                );
            }

            return null;
        });
    }

    @Override
    public List<BookingAdminViewDTO> getAllBookingsForAdmin() {

        return tx.execute(conn ->
                bookingDAO.findAllForAdmin(conn)
        );
    }

    @Override
    public List<BookingAdminViewDTO> getBookingsByStatus(BookingStatus status) {

        return tx.execute(conn ->
                bookingDAO.findByStatusForAdmin(conn, status)
        );
    }

    @Override
    public List<BookingAdminViewDTO> getBookingsPage(int page,
                                                     int pageSize) {

        int offset = (page - 1) * pageSize;

        return tx.execute(conn ->
                bookingDAO.findPageForAdmin(conn,
                        pageSize,
                        offset)
        );
    }

    @Override
    public int countBookings() {

        return tx.execute(conn ->
                bookingDAO.countAll(conn)
        );
    }

    @Override
    public List<BookingAdminViewDTO> getBookingsPageByStatus(BookingStatus status,
                                                             int page,
                                                             int pageSize) {

        int offset = (page - 1) * pageSize;

        return tx.execute(conn ->
                bookingDAO.findPageByStatusForAdmin(
                        conn,
                        status,
                        pageSize,
                        offset
                )
        );
    }

    @Override
    public int countBookingsByStatus(BookingStatus status) {

        return tx.execute(conn ->
                bookingDAO.countByStatus(conn, status)
        );
    }

    @Override
    public Optional<BookingAdminDetailDTO> getBookingDetail(Long bookingId) {

        return tx.execute(conn ->
                bookingDAO.findDetailForAdmin(conn, bookingId)
        );
    }

    @Override
    public List<CustomerBookingDTO> getBookingsByCustomer(Long customerId) {

        return tx.execute(conn ->
                bookingDAO.findByCustomer(conn, customerId)
        );
    }

    @Override
    public void expirePendingBookings() {

        tx.execute(conn -> {

            List<Booking> expired =
                    bookingDAO.findExpiredPending(conn, 24);

            for (Booking booking : expired) {

                bookingDAO.updateStatus(
                        conn,
                        booking.getId(),
                        BookingStatus.CANCELLED,
                        null
                );

                propertyService.failTransaction(
                        booking.getPropertyId(),
                        conn
                );
            }

            return null;
        });
    }
}