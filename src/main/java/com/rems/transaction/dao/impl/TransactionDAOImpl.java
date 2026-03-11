package com.rems.transaction.dao.impl;

import com.rems.common.constant.TransactionStatus;
import com.rems.transaction.dao.TransactionDAO;
import com.rems.transaction.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAOImpl implements TransactionDAO {

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

                tx.setCreatedAt(
                        rs.getTimestamp("created_at").toLocalDateTime());

                list.add(tx);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

}