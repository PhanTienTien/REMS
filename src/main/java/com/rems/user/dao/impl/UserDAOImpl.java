package com.rems.user.dao.impl;

import com.rems.common.base.BaseDAO;
import com.rems.common.constant.Role;
import com.rems.user.dao.UserDAO;
import com.rems.user.model.User;

import java.sql.*;

public class UserDAOImpl extends BaseDAO implements UserDAO {

    // ==============================
    // FIND BY EMAIL
    // ==============================
    public User findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ? AND is_deleted = FALSE";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    // ==============================
    // FIND BY AUTH ID (NEW)
    // ==============================
    public User findByAuthId(Long authId){

        String sql = "SELECT * FROM users WHERE auth_id = ? AND is_deleted = FALSE";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setLong(1, authId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    // ==============================
    // SAVE USER (NO PASSWORD HERE)
    // ==============================
    public void save(User user) {

        String sql = """
                INSERT INTO users
                (auth_id, full_name, email, phone_number, role, is_verified, is_deleted)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, user.getAuthId());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone_number());
            ps.setString(5, user.getRole().name());
            ps.setBoolean(6, user.isVerified());
            ps.setBoolean(7, user.isDeleted());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==============================
    // UPDATE VERIFIED
    // ==============================
    public void updateVerified(Long userId, boolean verified) {

        String sql = """
                UPDATE users
                SET is_verified = ?, updated_at = NOW()
                WHERE id = ?
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setBoolean(1, verified);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==============================
    // SOFT DELETE
    // ==============================
    public void softDelete(Long userId){

        String sql = """
                UPDATE users
                SET is_deleted = TRUE,
                    updated_at = NOW()
                WHERE id = ?
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==============================
    // MAP ROW
    // ==============================
    private User mapRow(ResultSet rs) throws SQLException {

        Timestamp updatedTs = rs.getTimestamp("updated_at");

        return new User(
                rs.getLong("id"),
                rs.getLong("auth_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone_number"),
                Role.valueOf(rs.getString("role")),
                rs.getBoolean("is_verified"),
                rs.getBoolean("is_deleted"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                updatedTs != null ? updatedTs.toLocalDateTime() : null
        );
    }
}