package com.rems.booking.dao;

import com.rems.booking.dto.BookingAdminDetailDTO;
import com.rems.booking.dto.BookingAdminViewDTO;
import com.rems.booking.dto.CustomerBookingDTO;
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

    boolean existsActiveBooking(Connection conn,
                                Long propertyId,
                                Long customerId);

    List<BookingAdminViewDTO> findAllForAdmin(Connection conn);

    List<BookingAdminViewDTO> findByStatusForAdmin(Connection conn,
                                                   BookingStatus status);

    List<BookingAdminViewDTO> findPageForAdmin(Connection conn,
                                               int limit,
                                               int offset);

    int countAll(Connection conn);

    List<BookingAdminViewDTO> findPageByStatusForAdmin(Connection conn,
                                                       BookingStatus status,
                                                       int limit,
                                                       int offset);

    int countByStatus(Connection conn,
                      BookingStatus status);

    Optional<BookingAdminDetailDTO> findDetailForAdmin(Connection conn,
                                                       Long bookingId);

    List<CustomerBookingDTO> findByCustomer(Connection conn,
                                            Long customerId);

    List<Booking> findExpiredPending(Connection conn,
                                     int hours);

    public List<BookingAdminViewDTO> search(
            Connection conn,
            String keyword,
            BookingStatus status,
            String sort,
            int limit,
            int offset);

    public int countSearch(
            Connection conn,
            String keyword,
            BookingStatus status);

    void rejectOtherBookings(Connection conn,
                             Long propertyId,
                             Long acceptedBookingId,
                             Long staffId);

}
