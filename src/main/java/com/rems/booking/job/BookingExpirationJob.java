package com.rems.booking.job;

import com.rems.booking.service.BookingService;
import com.rems.booking.service.impl.BookingServiceImpl;
import com.rems.common.transaction.TransactionManager;
import com.rems.property.service.impl.PropertyServiceImpl;
import com.rems.transaction.service.impl.TransactionServiceImpl;

public class BookingExpirationJob {

    private TransactionManager txManager;

    private final BookingService bookingService =
            new BookingServiceImpl(new PropertyServiceImpl(), new TransactionServiceImpl(txManager));

    public void run() {

        bookingService.expirePendingBookings();
    }
}
