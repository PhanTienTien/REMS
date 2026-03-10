package com.rems.user.dao.impl;

import com.rems.common.constant.Role;
import com.rems.common.util.PasswordUtil;
import com.rems.user.dao.UserDAO;
import com.rems.user.model.User;
import com.rems.user.model.dto.CreateUserByAdminDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    @Override
    public List<User> findAll(Connection conn) {

        String sql = """
        SELECT *
        FROM users
        WHERE is_deleted = FALSE 
        ORDER BY created_at DESC
    """;

        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

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

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
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
            ps.setString(4, user.getPhoneNumber());
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
            throw new RuntimeException("Error inserting user", e);
        }
    }

    @Override
    public void update(Connection conn, User user) {

        String sql = """
        UPDATE users
        SET full_name = ?,
            phone_number = ?,
            role = ?,
            is_verified = ?,
            updated_at = NOW()
        WHERE id = ?
          AND is_deleted = FALSE
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhoneNumber());
            ps.setString(3, user.getRole().name());
            ps.setBoolean(4, user.isVerified());
            ps.setLong(5, user.getId());

            ps.executeUpdate();

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

    @Override
    public void restore(Connection conn, Long userId) {

        String sql = """
        UPDATE users
        SET is_deleted = FALSE,
            updated_at = NOW()
        WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long countUsers(Connection conn,
                           String keyword,
                           String role) {

        String sql = """
        SELECT COUNT(*)
        FROM users
        WHERE is_deleted = FALSE
        AND (
                ? IS NULL
                OR full_name LIKE CONCAT('%', ?, '%')
                OR email LIKE CONCAT('%', ?, '%')
                OR id = ?
            )
        AND (? IS NULL OR role = ?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);

            if (keyword != null && keyword.matches("\\d+")) {
                ps.setLong(4, Long.parseLong(keyword));
            } else {
                ps.setNull(4, Types.BIGINT);
            }

            ps.setString(5, role);
            ps.setString(6, role);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getLong(1);
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    @Override
    public List<User> search(Connection conn,
                             String keyword,
                             String role,
                             Boolean active,
                             int page,
                             int size) {

        String sql = """
        SELECT *
        FROM users
        WHERE is_deleted = FALSE
        AND (
                ? IS NULL
                OR full_name LIKE CONCAT('%', ?, '%')
                OR email LIKE CONCAT('%', ?, '%')
                OR id = ?
            )
        AND (? IS NULL OR role = ?)
        LIMIT ? OFFSET ?
    """;

        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);

            if (keyword != null && keyword.matches("\\d+")) {
                ps.setLong(4, Long.parseLong(keyword));
            } else {
                ps.setNull(4, Types.BIGINT);
            }

            ps.setString(5, role);
            ps.setString(6, role);

            ps.setInt(7, size);
            ps.setInt(8, (page - 1) * size);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    users.add(mapRow(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public void saveCreateByAdmin(Connection conn, CreateUserByAdminDTO createUserByAdminDTO) {
        try {
            String authSql = """
                INSERT INTO auth_accounts
                (email, phone_number, password_hash, status)
                VALUES (?, ?, ?, ?)
                """;

            Long authId;

            try (PreparedStatement ps = conn.prepareStatement(authSql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, createUserByAdminDTO.getEmail());
                ps.setString(2, createUserByAdminDTO.getPhoneNumber());
                ps.setString(3, PasswordUtil.hash(createUserByAdminDTO.getPassword()));
                ps.setString(4, createUserByAdminDTO.getStatus().name());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new RuntimeException("Cannot get generated auth_id");
                    }
                    authId = rs.getLong(1);
                }
            }

            String userSql = """
                INSERT INTO users
                (auth_id, full_name, email, phone_number, role)
                VALUES (?, ?, ?, ?, ?)
                """;

            try (PreparedStatement ps = conn.prepareStatement(userSql)) {

                ps.setLong(1, authId);
                ps.setString(2, createUserByAdminDTO.getFullName());
                ps.setString(3, createUserByAdminDTO.getEmail());
                ps.setString(4, createUserByAdminDTO.getPhoneNumber());
                ps.setString(3, createUserByAdminDTO.getRole().name());

                ps.executeUpdate();
            }

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