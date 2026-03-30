package com.rems.booking.dao.impl;

import com.rems.booking.dao.BookingDAO;
import com.rems.booking.dto.BookingAdminDetailDTO;
import com.rems.booking.dto.BookingAdminViewDTO;
import com.rems.booking.dto.CustomerBookingDTO;
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
            accepted_at = CASE
                WHEN ? = 'ACCEPTED' THEN NOW()
                ELSE accepted_at
            END,
            updated_at = NOW()
        WHERE id = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setObject(2, staffId);
            ps.setString(3, status.name());
            ps.setLong(4, bookingId);

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

    @Override
    public boolean existsActiveBooking(Connection conn,
                                       Long propertyId,
                                       Long customerId) {

        String sql = """
        SELECT 1
        FROM bookings
        WHERE property_id = ?
        AND customer_id = ?
        AND status IN ('PENDING','ACCEPTED')
        LIMIT 1
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, propertyId);
            ps.setLong(2, customerId);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookingAdminViewDTO> findAllForAdmin(Connection conn) {

        String sql = """
        SELECT
        b.id,
        p.title,
        u.full_name,
        b.status,
        b.created_at
        FROM bookings b
        JOIN properties p ON p.id = b.property_id
        JOIN users u ON u.id = b.customer_id
        ORDER BY b.created_at DESC
    """;

        List<BookingAdminViewDTO> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                BookingAdminViewDTO v = new BookingAdminViewDTO();

                v.setBookingId(rs.getLong("id"));
                v.setPropertyTitle(rs.getString("title"));
                v.setCustomerName(rs.getString("full_name"));

                v.setStatus(
                        BookingStatus.valueOf(
                                rs.getString("status")
                        )
                );

                v.setCreatedAt(
                        rs.getTimestamp("created_at")
                                .toLocalDateTime()
                );

                list.add(v);
            }

            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookingAdminViewDTO> findByStatusForAdmin(Connection conn,
                                                          BookingStatus status) {

        String sql = """
        SELECT
        b.id,
        p.title,
        u.full_name,
        b.status,
        b.created_at
        FROM bookings b
        JOIN properties p ON p.id = b.property_id
        JOIN users u ON u.id = b.customer_id
        WHERE b.status = ?
        ORDER BY b.created_at DESC
    """;

        List<BookingAdminViewDTO> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                BookingAdminViewDTO v = new BookingAdminViewDTO();

                v.setBookingId(rs.getLong("id"));
                v.setPropertyTitle(rs.getString("title"));
                v.setCustomerName(rs.getString("full_name"));

                v.setStatus(
                        BookingStatus.valueOf(
                                rs.getString("status")
                        )
                );

                v.setCreatedAt(
                        rs.getTimestamp("created_at")
                                .toLocalDateTime()
                );

                list.add(v);
            }

            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookingAdminViewDTO> findPageForAdmin(Connection conn,
                                                      int limit,
                                                      int offset) {

        String sql = """
        SELECT
        b.id,
        p.title,
        u.full_name,
        b.status,
        b.created_at
        FROM bookings b
        JOIN properties p ON p.id = b.property_id
        JOIN users u ON u.id = b.customer_id
        ORDER BY b.created_at DESC
        LIMIT ? OFFSET ?
    """;

        List<BookingAdminViewDTO> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                BookingAdminViewDTO v = new BookingAdminViewDTO();

                v.setBookingId(rs.getLong("id"));
                v.setPropertyTitle(rs.getString("title"));
                v.setCustomerName(rs.getString("full_name"));

                v.setStatus(
                        BookingStatus.valueOf(rs.getString("status"))
                );

                v.setCreatedAt(
                        rs.getTimestamp("created_at").toLocalDateTime()
                );

                list.add(v);
            }

            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countAll(Connection conn) {

        String sql = "SELECT COUNT(*) FROM bookings";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            rs.next();

            return rs.getInt(1);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookingAdminViewDTO> findPageByStatusForAdmin(Connection conn,
                                                              BookingStatus status,
                                                              int limit,
                                                              int offset) {

        String sql = """
        SELECT
            b.id,
            p.title AS property_title,
            u.full_name AS customer_name,
            b.status,
            b.created_at
        FROM bookings b
        JOIN properties p ON p.id = b.property_id
        JOIN users u ON u.id = b.customer_id
        WHERE b.status = ?
        ORDER BY b.created_at DESC
        LIMIT ? OFFSET ?
    """;

        List<BookingAdminViewDTO> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                BookingAdminViewDTO dto = new BookingAdminViewDTO();

                dto.setBookingId(rs.getLong("id"));
                dto.setPropertyTitle(rs.getString("property_title"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setStatus(BookingStatus.valueOf(rs.getString("status")));
                dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                list.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public int countByStatus(Connection conn,
                             BookingStatus status) {

        String sql = """
        SELECT COUNT(*)
        FROM bookings
        WHERE status = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    @Override
    public Optional<BookingAdminDetailDTO> findDetailForAdmin(Connection conn,
                                                              Long bookingId) {

        String sql = """
        SELECT
            b.id,
            b.status,
            b.note,
            b.created_at,
            b.accepted_at,

            p.title AS property_title,
            p.address AS property_address,

            u.full_name AS customer_name,
            u.email AS customer_email,

            s.full_name AS staff_name

        FROM bookings b

        JOIN properties p ON p.id = b.property_id
        JOIN users u ON u.id = b.customer_id

        LEFT JOIN users s ON s.id = b.accepted_by

        WHERE b.id = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, bookingId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                BookingAdminDetailDTO dto =
                        new BookingAdminDetailDTO();

                dto.setBookingId(rs.getLong("id"));
                dto.setStatus(rs.getString("status"));
                dto.setNote(rs.getString("note"));

                dto.setPropertyTitle(rs.getString("property_title"));
                dto.setPropertyAddress(rs.getString("property_address"));

                dto.setCustomerName(rs.getString("customer_name"));
                dto.setCustomerEmail(rs.getString("customer_email"));

                dto.setStaffName(rs.getString("staff_name"));

                dto.setCreatedAt(
                        rs.getTimestamp("created_at").toLocalDateTime()
                );

                if (rs.getTimestamp("accepted_at") != null) {
                    dto.setAcceptedAt(
                            rs.getTimestamp("accepted_at")
                                    .toLocalDateTime()
                    );
                }

                return Optional.of(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public List<CustomerBookingDTO> findByCustomer(Connection conn,
                                                   Long customerId) {

        String sql = """
        SELECT
            b.id,
            b.status,
            b.note,
            b.created_at,
            p.title AS property_title
        FROM bookings b
        JOIN properties p ON p.id = b.property_id
        WHERE b.customer_id = ?
        ORDER BY b.created_at DESC
    """;

        List<CustomerBookingDTO> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CustomerBookingDTO dto = new CustomerBookingDTO();

                dto.setBookingId(rs.getLong("id"));
                dto.setPropertyTitle(rs.getString("property_title"));
                dto.setStatus(rs.getString("status"));
                dto.setNote(rs.getString("note"));

                dto.setCreatedAt(
                        rs.getTimestamp("created_at")
                                .toLocalDateTime()
                );

                list.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public List<Booking> findExpiredPending(Connection conn,
                                            int hours) {

        String sql = """
        SELECT *
        FROM bookings
        WHERE status = 'PENDING'
        AND created_at < NOW() - INTERVAL ? HOUR
    """;

        List<Booking> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hours);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Booking booking = map(rs);
                list.add(booking);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public List<BookingAdminViewDTO> search(
            Connection conn,
            String keyword,
            BookingStatus status,
            String sort,
            int limit,
            int offset) {

        StringBuilder sql = new StringBuilder("""
        SELECT b.id, p.title, u.full_name, b.status, b.created_at
        FROM bookings b
        JOIN properties p ON p.id = b.property_id
        JOIN users u ON u.id = b.customer_id
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (p.title LIKE ? OR u.full_name LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        if (status != null) {
            sql.append(" AND b.status = ?");
            params.add(status.name());
        }

        // SORT
        if ("oldest".equals(sort)) {
            sql.append(" ORDER BY b.created_at ASC");
        } else {
            sql.append(" ORDER BY b.created_at DESC");
        }

        sql.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            List<BookingAdminViewDTO> list = new ArrayList<>();

            while (rs.next()) {

                BookingAdminViewDTO dto = new BookingAdminViewDTO();

                dto.setBookingId(rs.getLong("id"));
                dto.setPropertyTitle(rs.getString("title"));
                dto.setCustomerName(rs.getString("full_name"));
                dto.setStatus(BookingStatus.valueOf(rs.getString("status")));
                dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                list.add(dto);
            }

            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countSearch(
            Connection conn,
            String keyword,
            BookingStatus status) {

        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*)
        FROM bookings b
        JOIN properties p ON p.id = b.property_id
        JOIN users u ON u.id = b.customer_id
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (p.title LIKE ? OR u.full_name LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        if (status != null) {
            sql.append(" AND b.status = ?");
            params.add(status.name());
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            rs.next();
            return rs.getInt(1);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}