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

    List<BookingAdminViewDTO> getAllBookingsForAdmin();

    List<BookingAdminViewDTO> getBookingsByStatus(BookingStatus status);

    List<BookingAdminViewDTO> getBookingsPage(int page,
                                              int pageSize);

    int countBookings();

    List<BookingAdminViewDTO> getBookingsPageByStatus(BookingStatus status,
                                                      int page,
                                                      int pageSize);

    int countBookingsByStatus(BookingStatus status);

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
