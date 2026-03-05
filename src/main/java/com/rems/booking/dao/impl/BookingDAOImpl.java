package com.rems.booking.dao.impl;

import com.rems.booking.dao.BookingDAO;
import com.rems.booking.model.Booking;
import com.rems.common.constant.BookingStatus;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public Long insert(Connection conn, Booking booking) {

        String sql = """
        INSERT INTO bookings
        (property_id, customer_id, status, note)
        VALUES (?, ?, ?, ?)
    """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, booking.getPropertyId());
            ps.setLong(2, booking.getCustomerId());
            ps.setString(3, booking.getStatus().name());
            ps.setString(4, booking.getNote());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new RuntimeException("Failed to generate booking ID");

        } catch (SQLException e) {
            throw new RuntimeException("Insert booking failed", e);
        }
    }

    @Override
    public Optional<Booking> findByIdForUpdate(Connection conn, Long id) {

        String sql = """
        SELECT * FROM bookings
        WHERE id = ?
        FOR UPDATE
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Find booking for update failed", e);
        }
    }

    @Override
    public List<Booking> findPendingByPropertyForUpdate(Connection conn,
                                                        Long propertyId) {

        String sql = """
        SELECT * FROM bookings
        WHERE property_id = ?
          AND status = 'PENDING'
        FOR UPDATE
    """;

        List<Booking> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, propertyId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Find pending bookings failed", e);
        }
    }

    @Override
    public boolean existsAcceptedByProperty(Connection conn,
                                            Long propertyId) {

        String sql = """
        SELECT 1 FROM bookings
        WHERE property_id = ?
          AND status = 'ACCEPTED'
        LIMIT 1
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, propertyId);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Check accepted booking failed", e);
        }
    }

    @Override
    public void updateStatus(Connection conn,
                             Long bookingId,
                             BookingStatus status,
                             Long staffId) {

        String sql = """
        UPDATE bookings
        SET status = ?,
            accepted_by = ?,
            accepted_at = ?,
            updated_at = NOW()
        WHERE id = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());

            if (status == BookingStatus.ACCEPTED) {
                ps.setLong(2, staffId);
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            } else {
                ps.setNull(2, Types.BIGINT);
                ps.setNull(3, Types.TIMESTAMP);
            }

            ps.setLong(4, bookingId);

            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new BusinessException(ErrorCode.NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Update booking status failed", e);
        }
    }

    private Booking mapRow(ResultSet rs) throws SQLException {

        Booking booking = new Booking();

        booking.setId(rs.getLong("id"));
        booking.setPropertyId(rs.getLong("property_id"));
        booking.setCustomerId(rs.getLong("customer_id"));
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
        booking.setNote(rs.getString("note"));

        Long acceptedBy = rs.getLong("accepted_by");
        if (!rs.wasNull()) booking.setAcceptedBy(acceptedBy);

        Timestamp acceptedAt = rs.getTimestamp("accepted_at");
        if (acceptedAt != null)
            booking.setAcceptedAt(acceptedAt.toLocalDateTime());

        booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null)
            booking.setUpdatedAt(updatedAt.toLocalDateTime());

        return booking;
    }

    //test
    public Long insertTestBooking(Connection conn, Long propertyId, Long customerId) {

        String sql = """
        INSERT INTO bookings
        (property_id, customer_id, status, created_at)
        VALUES (?, ?, 'PENDING', NOW())
        """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, propertyId);
            ps.setLong(2, customerId);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new RuntimeException("Cannot get booking id");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getBookingStatus(Connection conn, Long bookingId) {

        String sql = "SELECT status FROM bookings WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, bookingId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("status");
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
