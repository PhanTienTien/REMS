package com.rems.booking.service.impl;

import com.rems.booking.dao.BookingDAO;
import com.rems.booking.dao.impl.BookingDAOImpl;
import com.rems.booking.model.Booking;
import com.rems.booking.service.BookingService;
import com.rems.common.constant.BookingStatus;
import com.rems.common.transaction.TransactionManager;
import com.rems.property.service.PropertyService;

import java.util.List;

public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO = new BookingDAOImpl();

    private final TransactionManager tx = new TransactionManager();

    private final PropertyService propertyService;

    public BookingServiceImpl(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Override
    public Long createBooking(Long propertyId,
                              Long customerId,
                              String note) {

        return tx.execute(conn -> {

            Booking booking = new Booking();

            booking.setPropertyId(propertyId);
            booking.setCustomerId(customerId);
            booking.setNote(note);

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
    public void cancelBooking(Long bookingId, Long customerId) {

        tx.executeWithoutResult(conn -> {

            Booking booking = bookingDAO
                    .findByIdForUpdate(conn, bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            if (!booking.getCustomerId().equals(customerId)) {
                throw new RuntimeException("Unauthorized");
            }

            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new RuntimeException("Cannot cancel");
            }

            bookingDAO.updateStatus(conn,
                    bookingId,
                    BookingStatus.CANCELLED,
                    null);
        });
    }
}