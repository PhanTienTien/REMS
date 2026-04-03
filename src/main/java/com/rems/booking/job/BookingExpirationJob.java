package com.rems.booking.job;

import com.rems.booking.service.BookingService;
import com.rems.common.util.Factory;

public class BookingExpirationJob {

    private final BookingService bookingService = Factory.getBookingService();

    public void run() {
        bookingService.expirePendingBookings();
    }
}
