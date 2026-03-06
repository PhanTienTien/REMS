package com.rems.user.dao.impl;

import com.rems.common.constant.Role;
import com.rems.common.util.DBUtil;
import com.rems.user.dao.UserDAO;
import com.rems.user.model.User;

import java.sql.*;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    @Override
    public Optional<User> findById(Connection conn, Long id) {

        String sql = """
        SELECT 
            id,
            auth_id,
            full_name,
            email,
            phone_number,
            role,
            is_verified,
            is_deleted,
            created_at,
            updated_at
        FROM users
        WHERE id = ?
          AND is_deleted = FALSE
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    User user = mapRow(rs);
                    return Optional.of(user);
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by id", e);
        }
    }

    @Override
    public Optional<User> findByEmail(Connection conn, String email) {

        String sql = "SELECT * FROM users WHERE email = ? AND is_deleted = FALSE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByAuthId(Connection conn, Long authId){

        String sql = "SELECT * FROM users WHERE auth_id = ? AND is_deleted = FALSE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, authId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                User user = new User();

                user.setId(rs.getLong("id"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(Role.valueOf(rs.getString("role")));

                return Optional.of(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(Connection conn, User user) {

        String sql = """
                INSERT INTO users
                (auth_id, full_name, email, phone_number, role, is_verified, is_deleted)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

    @Override
    public void updateVerified(Connection conn, Long userId, boolean verified) {

        String sql = """
                UPDATE users
                SET is_verified = ?, updated_at = NOW()
                WHERE id = ?
                  AND is_deleted = FALSE
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, verified);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void softDelete(Connection conn, Long userId){

        String sql = """
                UPDATE users
                SET is_deleted = TRUE,
                    updated_at = NOW()
                WHERE id = ?
                  AND is_deleted = FALSE
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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