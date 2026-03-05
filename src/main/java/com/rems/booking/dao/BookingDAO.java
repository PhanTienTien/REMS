package com.rems.booking.dao;

import com.rems.booking.model.Booking;
import com.rems.common.constant.BookingStatus;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface BookingDAO {

    Optional<Booking> findByIdForUpdate(Connection conn, Long id);

    List<Booking> findPendingByPropertyForUpdate(Connection conn, Long propertyId);

    boolean existsAcceptedByProperty(Connection conn, Long propertyId);

    void updateStatus(Connection conn,
                      Long bookingId,
                      BookingStatus status,
                      Long staffId);

    Long insert(Connection conn, Booking booking);

    //test
    Long insertTestBooking(Connection conn, Long propertyId, Long customerId);
    String getBookingStatus(Connection conn, Long bookingId);
}
