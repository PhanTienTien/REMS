package com.rems.auth.dao.impl;

import com.rems.auth.dao.AuthAccountDAO;
import com.rems.auth.model.AuthAccount;
import com.rems.common.base.BaseDAO;
import com.rems.common.constant.AccountStatus;

import java.sql.*;
import java.time.LocalDateTime;

public class AuthAccountDAOImpl extends BaseDAO implements AuthAccountDAO {

    // =====================================
    // FIND BY USERNAME
    // =====================================
    public AuthAccount findByUserName(String userName) {

        String sql = "SELECT * FROM auth_accounts WHERE userName = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setString(1, userName);

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

    public AuthAccount findByEmail(String email) {

        String sql = "SELECT * FROM auth_accounts WHERE email = ?";

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

    // =====================================
    // FIND BY ID (optional but recommended)
    // =====================================
    public AuthAccount findById(Long id) {

        String sql = "SELECT * FROM auth_accounts WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setLong(1, id);

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

    // =====================================
    // SAVE ACCOUNT
    // =====================================
    public Long save(AuthAccount account) {

        String sql = """
                INSERT INTO auth_accounts
                (userName, email, phone_number, password_hash, provider, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, account.getUserName());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPhone_number());
            ps.setString(4, account.getPasswordHash());
            ps.setString(5, account.getProvider());
            ps.setString(6, account.getStatus().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    // =====================================
    // UPDATE STATUS (ACTIVE / LOCKED)
    // =====================================
    public void updateStatus(Long authId, AccountStatus status) {

        String sql = """
                UPDATE auth_accounts
                SET status = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setLong(2, authId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // =====================================
    // INCREASE LOGIN ATTEMPT
    // =====================================
    public void increaseLoginAttempt(Long authId,
                                     LocalDateTime now) {

        String sql = """
                UPDATE auth_accounts
                SET login_attempt = login_attempt + 1,
                    last_login_attempt = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(now));
            ps.setLong(2, authId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // =====================================
    // RESET LOGIN ATTEMPT
    // =====================================
    public void resetLoginAttempt(Long authId) {

        String sql = """
                UPDATE auth_accounts
                SET login_attempt = 0,
                    last_login_attempt = NULL
                WHERE id = ?
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setLong(1, authId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // =====================================
    // MAP ROW
    // =====================================
    private AuthAccount mapRow(ResultSet rs) throws SQLException {

        return new AuthAccount(
                rs.getLong("id"),
                rs.getString("userName"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("password_hash"),
                rs.getString("provider"),
                rs.getString("provider_id"),
                AccountStatus.valueOf(rs.getString("status")),
                rs.getInt("login_attempt"),
                rs.getTimestamp("last_login_attempt") != null
                        ? rs.getTimestamp("last_login_attempt").toLocalDateTime()
                        : null,
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}