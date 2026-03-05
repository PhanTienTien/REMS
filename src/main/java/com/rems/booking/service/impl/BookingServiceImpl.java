package com.rems.booking.service.impl;

import com.rems.booking.dao.BookingDAO;
import com.rems.booking.dao.impl.BookingDAOImpl;
import com.rems.booking.model.Booking;
import com.rems.booking.service.BookingService;
import com.rems.common.constant.BookingStatus;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.transaction.TransactionManager;
import com.rems.common.util.DBUtil;
import com.rems.property.service.PropertyService;
import com.rems.property.service.impl.PropertyServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO = new BookingDAOImpl();
    private final PropertyService propertyService = new PropertyServiceImpl();

    public BookingServiceImpl(TransactionManager txManager) {
    }

    @Override
    public Long createBooking(Long propertyId,
                              Long customerId,
                              String note) {

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            try {

                Booking booking = new Booking();
                booking.setPropertyId(propertyId);
                booking.setCustomerId(customerId);
                booking.setStatus(BookingStatus.PENDING);
                booking.setNote(note);

                Long id = bookingDAO.insert(conn, booking);

                conn.commit();
                return id;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void acceptBooking(Long bookingId, Long staffId) {

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            try {

                // 1️⃣ Lock booking
                Booking booking = bookingDAO
                        .findByIdForUpdate(conn, bookingId)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.NOT_FOUND));

                if (booking.getStatus() != BookingStatus.PENDING)
                    throw new BusinessException(
                            ErrorCode.INVALID_STATE_TRANSITION);

                Long propertyId = booking.getPropertyId();

                // 2️⃣ Lock property (critical)
                propertyService.reserveProperty(propertyId, conn);
                // reserveProperty internally:
                // SELECT property FOR UPDATE
                // check AVAILABLE
                // set RESERVED

                // 3️⃣ Double safety: ensure no accepted exists
                if (bookingDAO.existsAcceptedByProperty(conn, propertyId))
                    throw new BusinessException(
                            ErrorCode.BOOKING_ALREADY_ACCEPTED);

                // 4️⃣ Mark current booking ACCEPTED
                bookingDAO.updateStatus(conn,
                        bookingId,
                        BookingStatus.ACCEPTED,
                        staffId);

                // 5️⃣ Reject all other PENDING bookings
                List<Booking> pendings =
                        bookingDAO.findPendingByPropertyForUpdate(
                                conn, propertyId);

                for (Booking b : pendings) {
                    if (!b.getId().equals(bookingId)) {
                        bookingDAO.updateStatus(conn,
                                b.getId(),
                                BookingStatus.REJECTED,
                                staffId);
                    }
                }

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rejectBooking(Long bookingId, Long staffId) {

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            try {

                Booking booking = bookingDAO
                        .findByIdForUpdate(conn, bookingId)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.NOT_FOUND));

                if (booking.getStatus() != BookingStatus.PENDING)
                    throw new BusinessException(
                            ErrorCode.INVALID_STATE_TRANSITION);

                bookingDAO.updateStatus(conn,
                        bookingId,
                        BookingStatus.REJECTED,
                        staffId);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelBooking(Long bookingId, Long customerId) {

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            try {

                Booking booking = bookingDAO
                        .findByIdForUpdate(conn, bookingId)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.NOT_FOUND));

                if (!booking.getCustomerId().equals(customerId))
                    throw new BusinessException(ErrorCode.FORBIDDEN);

                if (booking.getStatus() != BookingStatus.PENDING)
                    throw new BusinessException(
                            ErrorCode.INVALID_STATE_TRANSITION);

                bookingDAO.updateStatus(conn,
                        bookingId,
                        BookingStatus.CANCELLED,
                        null);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
