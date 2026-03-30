package com.rems.transaction.dao.impl;

import com.rems.common.constant.PropertyType;
import com.rems.common.constant.TransactionStatus;
import com.rems.transaction.dao.TransactionDAO;
import com.rems.transaction.model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAOImpl implements TransactionDAO {

    private Transaction map(ResultSet rs) throws SQLException {

        Transaction tx = new Transaction();

        tx.setId(rs.getLong("id"));
        tx.setBookingId(rs.getLong("booking_id"));
        tx.setPropertyId(rs.getLong("property_id"));
        tx.setCustomerId(rs.getLong("customer_id"));

        tx.setAmount(rs.getBigDecimal("amount"));
        tx.setType(PropertyType.valueOf(rs.getString("type")));

        tx.setPropertyTitleSnapshot(rs.getString("property_title_snapshot"));
        tx.setPropertyPriceSnapshot(rs.getBigDecimal("property_price_snapshot"));
        tx.setCustomerNameSnapshot(rs.getString("customer_name_snapshot"));

        tx.setStatus(TransactionStatus.valueOf(rs.getString("status")));

        tx.setProcessedBy((Long) rs.getObject("processed_by"));

        Timestamp completedAt = rs.getTimestamp("completed_at");
        if (completedAt != null) {
            tx.setCompletedAt(completedAt.toLocalDateTime());
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            tx.setCreatedAt(createdAt.toLocalDateTime());
        }

        return tx;
    }

    @Override
    public Optional<Transaction> findById(Connection conn, Long id) {

        String sql = """
        SELECT *
        FROM transactions
        WHERE id = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Transaction tx = new Transaction();

                tx.setId(rs.getLong("id"));
                tx.setBookingId(rs.getLong("booking_id"));
                tx.setPropertyId(rs.getLong("property_id"));
                tx.setCustomerId(rs.getLong("customer_id"));

                tx.setAmount(rs.getBigDecimal("amount"));

                tx.setType(
                        PropertyType.valueOf(
                                rs.getString("type")));

                tx.setStatus(
                        TransactionStatus.valueOf(
                                rs.getString("status")));

                tx.setPropertyTitleSnapshot(
                        rs.getString("property_title_snapshot"));

                tx.setPropertyPriceSnapshot(
                        rs.getBigDecimal("property_price_snapshot"));

                tx.setCustomerNameSnapshot(
                        rs.getString("customer_name_snapshot"));

                tx.setCreatedAt(
                        rs.getTimestamp("created_at")
                                .toLocalDateTime());

                if (rs.getTimestamp("completed_at") != null) {
                    tx.setCompletedAt(
                            rs.getTimestamp("completed_at")
                                    .toLocalDateTime());
                }

                tx.setProcessedBy(
                        rs.getObject("processed_by", Long.class)
                );

                return Optional.of(tx);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Find transaction failed", e);
        }
    }

    @Override
    public Long insert(Connection conn, Transaction tx) {

        String sql = """
    INSERT INTO transactions
    (
        booking_id,
        property_id,
        customer_id,
        type,
        amount,
        property_price_snapshot,
        property_title_snapshot,
        customer_name_snapshot,
        status,
        completed_at,
        processed_by
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, tx.getBookingId());
            ps.setLong(2, tx.getPropertyId());
            ps.setLong(3, tx.getCustomerId());

            ps.setString(4, tx.getType().name());
            ps.setBigDecimal(5, tx.getAmount());

            ps.setBigDecimal(6, tx.getPropertyPriceSnapshot());
            ps.setString(7, tx.getPropertyTitleSnapshot());
            ps.setString(8, tx.getCustomerNameSnapshot());

            ps.setString(9, tx.getStatus().name());

            if (tx.getCompletedAt() != null) {
                ps.setTimestamp(10, Timestamp.valueOf(tx.getCompletedAt()));
            } else {
                ps.setNull(10, Types.TIMESTAMP);
            }

            if (tx.getProcessedBy() != null) {
                ps.setLong(11, tx.getProcessedBy());
            } else {
                ps.setNull(11, Types.BIGINT);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }

            throw new RuntimeException("Create transaction failed");

        } catch (SQLException e) {
            throw new RuntimeException("Insert transaction failed", e);
        }
    }

    @Override
    public Optional<Transaction> findByBookingId(Connection conn,
                                                 Long bookingId) {

        String sql = """
        SELECT * FROM transactions
        WHERE booking_id = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Transaction tx = new Transaction();

                tx.setId(rs.getLong("id"));
                tx.setBookingId(rs.getLong("booking_id"));
                tx.setPropertyId(rs.getLong("property_id"));
                tx.setCustomerId(rs.getLong("customer_id"));

                tx.setAmount(rs.getBigDecimal("amount"));

                tx.setStatus(
                        TransactionStatus.valueOf(
                                rs.getString("status")));

                return Optional.of(tx);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Find transaction failed", e);
        }
    }

    @Override
    public List<Transaction> findAll(Connection conn) {

        String sql = """
        SELECT *
        FROM transactions
        ORDER BY created_at DESC
    """;

        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Transaction tx = new Transaction();

                tx.setId(rs.getLong("id"));
                tx.setBookingId(rs.getLong("booking_id"));
                tx.setPropertyTitleSnapshot(
                        rs.getString("property_title_snapshot"));
                tx.setCustomerNameSnapshot(
                        rs.getString("customer_name_snapshot"));
                tx.setAmount(rs.getBigDecimal("amount"));

                tx.setStatus(
                        TransactionStatus.valueOf(
                                rs.getString("status")));
                tx.setType(
                        PropertyType.valueOf(
                                rs.getString("type")
                        )
                );

                tx.setProcessedBy(
                        rs.getObject("processed_by", Long.class)
                );

                tx.setCreatedAt(
                        rs.getTimestamp("created_at").toLocalDateTime());

                list.add(tx);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public List<Transaction> findByStatus(Connection conn,
                                          TransactionStatus status) {

        String sql = """
        SELECT *
        FROM transactions
        WHERE status = ?
        ORDER BY created_at DESC
    """;

        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Transaction tx = new Transaction();

                tx.setId(rs.getLong("id"));
                tx.setBookingId(rs.getLong("booking_id"));
                tx.setPropertyTitleSnapshot(
                        rs.getString("property_title_snapshot"));
                tx.setCustomerNameSnapshot(
                        rs.getString("customer_name_snapshot"));
                tx.setAmount(rs.getBigDecimal("amount"));

                tx.setStatus(
                        TransactionStatus.valueOf(
                                rs.getString("status")));

                tx.setCreatedAt(
                        rs.getTimestamp("created_at")
                                .toLocalDateTime());

                list.add(tx);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public Optional<Transaction> findByIdForUpdate(Connection conn, Long id) {

        String sql = """
            SELECT *
            FROM transactions
            WHERE id = ?
            FOR UPDATE
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(map(rs));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatus(Connection conn,
                             Long id,
                             TransactionStatus status,
                             Long staffId) {

        String sql = """
            UPDATE transactions
            SET status = ?,
                completed_at = ?,
                processed_by = ?
            WHERE id = ?
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            if (status == TransactionStatus.COMPLETED) {
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }
            ps.setObject(3, staffId);
            ps.setLong(4, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findByCustomer(Connection conn,
                                            Long customerId) {

        String sql = """
        SELECT *
        FROM transactions
        WHERE customer_id = ?
        ORDER BY created_at DESC
        """;

        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Transaction t = map(rs);

                list.add(t);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public List<Transaction> search(Connection conn,
                                    String keyword,
                                    TransactionStatus status,
                                    String sortBy,
                                    String sortDir,
                                    int offset,
                                    int limit) {

        StringBuilder sql = new StringBuilder("""
        SELECT *
        FROM transactions
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        // 🔍 keyword search
        if (keyword != null && !keyword.isBlank()) {
            sql.append("""
            AND (
                LOWER(property_title_snapshot) LIKE ?
                OR LOWER(customer_name_snapshot) LIKE ?
            )
        """);
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
        }

        // 🎯 filter status
        if (status != null) {
            sql.append(" AND status = ? ");
            params.add(status.name());
        }

        // 🔐 whitelist sort column
        String sortColumn = switch (sortBy) {
            case "amount" -> "amount";
            case "status" -> "status";
            default -> "created_at";
        };

        String direction = "asc".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";

        sql.append(" ORDER BY ").append(sortColumn).append(" ").append(direction);
        sql.append(" LIMIT ? OFFSET ?");

        params.add(limit);
        params.add(offset);

        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public int count(Connection conn,
                     String keyword,
                     TransactionStatus status) {

        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*)
        FROM transactions
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sql.append("""
            AND (
                LOWER(property_title_snapshot) LIKE ?
                OR LOWER(customer_name_snapshot) LIKE ?
            )
        """);

            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
        }

        if (status != null) {
            sql.append(" AND status = ? ");
            params.add(status.name());
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
