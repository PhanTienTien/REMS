package com.rems.booking.service;

public interface BookingService {

    Long createBooking(Long propertyId,
                       Long customerId,
                       String note);

    void acceptBooking(Long bookingId, Long staffId);

    void rejectBooking(Long bookingId, Long staffId);

    void cancelBooking(Long bookingId, Long customerId);
}
