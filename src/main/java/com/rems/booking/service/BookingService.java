package com.rems.booking.service;

import com.rems.booking.dto.BookingAdminDetailDTO;
import com.rems.booking.dto.BookingAdminViewDTO;
import com.rems.booking.dto.CustomerBookingDTO;
import com.rems.common.constant.BookingStatus;
import com.rems.common.util.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    Long createBooking(Long propertyId,
                       Long customerId,
                       String note,\
                       LocalDateTime scheduledAt\);

    void acceptBooking(Long bookingId, Long staffId);

    void rejectBooking(Long bookingId, Long staffId);

    void cancelBooking(Long bookingId, Long customerId);

    Optional<BookingAdminDetailDTO> getBookingDetail(Long bookingId);

    Optional<BookingAdminDetailDTO> getBookingDetailForStaff(Long bookingId,
                                                             Long staffId);

    List<CustomerBookingDTO> getBookingsByCustomer(Long customerId);

    PageResult<CustomerBookingDTO> getBookingsByCustomerPage(Long customerId, int page, int size);

    void expirePendingBookings();

    PageResult<BookingAdminViewDTO> searchBookings(
            String keyword,
            BookingStatus status,
            String sort,
            int page,
            int size
    );

    PageResult<BookingAdminViewDTO> searchBookingsByStaff(
            Long staffId,
            String keyword,
            BookingStatus status,
            String sort,
            int page,
            int size
    );

    void acceptBookingByStaff(Long bookingId, Long staffId);

    void rejectBookingByStaff(Long bookingId, Long staffId);
}
