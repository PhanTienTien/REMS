package com.rems.transaction.dao.impl;

import com.rems.transaction.dao.TransactionDAO;
import com.rems.transaction.model.Transaction;

import java.sql.*;
import java.util.Optional;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public Long insert(Connection conn, Transaction tx) {

        String sql = """
        INSERT INTO transactions
        (booking_id, property_id, customer_id,
         property_price_snapshot,
         property_title_snapshot,
         customer_name_snapshot,
         status, completed_at)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, tx.getBookingId());
            ps.setLong(2, tx.getPropertyId());
            ps.setLong(3, tx.getCustomerId());
            ps.setBigDecimal(4, tx.getPropertyPriceSnapshot());
            ps.setString(5, tx.getPropertyTitleSnapshot());
            ps.setString(6, tx.getCustomerNameSnapshot());
            ps.setString(7, tx.getStatus().name());

            if (tx.getCompletedAt() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(tx.getCompletedAt()));
            } else {
                ps.setNull(8, Types.TIMESTAMP);
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);

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

    //test
    public String getTransactionStatusByBookingId(Connection conn, Long bookingId) {

        String sql = "SELECT status FROM transactions WHERE booking_id = ?";

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