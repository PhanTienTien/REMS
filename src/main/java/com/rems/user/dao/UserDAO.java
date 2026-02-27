package com.rems.user.dao;

import com.rems.common.base.BaseDAO;
import com.rems.common.constant.AccountStatus;
import com.rems.common.constant.Role;
import com.rems.user.model.User;

import java.sql.*;
import java.time.LocalDateTime;

public class UserDAO extends BaseDAO {

    public UserDAO(Connection connection) {
        super(connection);
    }

    // ==============================
    // FIND BY EMAIL
    // ==============================
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // ==============================
    // SAVE USER
    // ==============================
    public void save(User user) throws SQLException {

        String sql = """
                INSERT INTO users
                (full_name, email, password, role, status, is_verified, login_attempt)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole().name());
            ps.setString(5, user.getStatus().name());
            ps.setBoolean(6, user.getVerified());
            ps.setInt(7, user.getLoginAttempt());

            ps.executeUpdate();

            // lấy id auto increment
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
        }
    }

    // ==============================
    // UPDATE STATUS
    // ==============================
    public void updateStatus(Long userId, AccountStatus status) throws SQLException {
        String sql = "UPDATE users SET status = ?, updated_at = NOW() WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    // ==============================
    // INCREASE LOGIN ATTEMPT
    // ==============================
    public void increaseLoginAttempt(Long userId, LocalDateTime now) throws SQLException {

        String sql = """
        UPDATE users
        SET login_attempt = login_attempt + 1,
            last_login_attempt = ?,
            updated_at = NOW()
        WHERE id = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(now));
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    // ==============================
    // RESET LOGIN ATTEMPT
    // ==============================
    public void resetLoginSuccess(Long userId) throws SQLException {

        String sql = """
        UPDATE users
        SET login_attempt = 0,
            last_login_attempt = NULL,
            login_attempt_window_start = NULL,
            updated_at = NOW()
        WHERE id = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }

    // ==============================
    // UPDATE VERIFIED
    // ==============================
    public void updateVerified(Long userId, boolean verified) throws SQLException {
        String sql = """
                UPDATE users
                SET is_verified = ?, updated_at = NOW()
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, verified);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    // ==============================
    // MAP RESULTSET → USER
    // ==============================
    private User mapRow(ResultSet rs) throws SQLException {

        Timestamp lastLoginTs = rs.getTimestamp("last_login_attempt");
        Timestamp windowTs = rs.getTimestamp("login_attempt_window_start");
        Timestamp updatedTs = rs.getTimestamp("updated_at");

        return new User(
                rs.getLong("id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role")),
                AccountStatus.valueOf(rs.getString("status")),
                rs.getBoolean("is_verified"),
                rs.getInt("login_attempt"),
                lastLoginTs != null ? lastLoginTs.toLocalDateTime() : null,
                windowTs != null ? windowTs.toLocalDateTime() : null,
                rs.getTimestamp("created_at").toLocalDateTime(),
                updatedTs != null ? updatedTs.toLocalDateTime() : null
        );
    }

    public void resetLoginWindow(Long userId, LocalDateTime now) {

        String sql = """
        UPDATE users
        SET login_attempt = 0,
            login_attempt_window_start = ?
        WHERE id = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(now));
            ps.setLong(2, userId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error resetting login window", e);
        }
    }
}