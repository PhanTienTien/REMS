package com.rems.booking.dao.impl;

import com.rems.booking.dao.BookingDAO;
import com.rems.booking.model.Booking;
import com.rems.common.constant.BookingStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDAOImpl implements BookingDAO {

    private Booking map(ResultSet rs) throws SQLException {

        Booking b = new Booking();

        b.setId(rs.getLong("id"));
        b.setPropertyId(rs.getLong("property_id"));
        b.setCustomerId(rs.getLong("customer_id"));

        b.setStatus(BookingStatus.valueOf(rs.getString("status")));

        b.setNote(rs.getString("note"));

        b.setAcceptedBy(rs.getObject("accepted_by", Long.class));

        Timestamp acceptedAt = rs.getTimestamp("accepted_at");
        if (acceptedAt != null) {
            b.setAcceptedAt(acceptedAt.toLocalDateTime());
        }

        b.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            b.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return b;
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
                return Optional.of(map(rs));
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> findPendingByPropertyForUpdate(Connection conn, Long propertyId) {

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
                list.add(map(rs));
            }

            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsAcceptedByProperty(Connection conn, Long propertyId) {

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

        } catch (Exception e) {
            throw new RuntimeException(e);
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
                accepted_at = NOW(),
                updated_at = NOW()
            WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setObject(2, staffId);
            ps.setLong(3, bookingId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long insert(Connection conn, Booking booking) {

        String sql = """
            INSERT INTO bookings(property_id, customer_id, note)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, booking.getPropertyId());
            ps.setLong(2, booking.getCustomerId());
            ps.setString(3, booking.getNote());

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
}