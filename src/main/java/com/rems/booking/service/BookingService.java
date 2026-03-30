package com.rems.booking.service;

import com.rems.booking.dto.BookingAdminDetailDTO;
import com.rems.booking.dto.BookingAdminViewDTO;
import com.rems.booking.dto.CustomerBookingDTO;
import com.rems.common.constant.BookingStatus;
import com.rems.common.util.PageResult;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Long createBooking(Long propertyId,
                       Long customerId,
                       String note);

    void acceptBooking(Long bookingId, Long staffId);

    void rejectBooking(Long bookingId, Long staffId);

    void cancelBooking(Long bookingId, Long customerId);

    Optional<BookingAdminDetailDTO> getBookingDetail(Long bookingId);

    List<CustomerBookingDTO> getBookingsByCustomer(Long customerId);

    void expirePendingBookings();

    PageResult<BookingAdminViewDTO> searchBookings(
            String keyword,
            BookingStatus status,
            String sort,
            int page,
            int size
    );
}
