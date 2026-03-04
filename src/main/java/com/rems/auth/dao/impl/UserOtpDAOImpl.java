package com.rems.auth.dao.impl;

import com.rems.auth.dao.UserOtpDAO;
import com.rems.auth.model.UserOtp;
import com.rems.common.base.BaseDAO;

import java.sql.*;

public class UserOtpDAOImpl extends BaseDAO implements UserOtpDAO {

    // ==========================
    // SAVE OTP
    // ==========================
    public void save(UserOtp otp) {

        String sql = """
                INSERT INTO user_otps
                (auth_id, otp_code, expired_at, attempt_count, is_used)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, otp.getAuthId());
            ps.setString(2, otp.getOtpCode());
            ps.setTimestamp(3, Timestamp.valueOf(otp.getExpiredAt()));
            ps.setInt(4, otp.getAttemptCount());
            ps.setBoolean(5, otp.isUsed());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    otp.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==========================
    // FIND LATEST OTP BY AUTH ID
    // ==========================
    public UserOtp findLatestByAuthId(Long authId){

        String sql = """
                SELECT *
                FROM user_otps
                WHERE auth_id = ?
                ORDER BY created_at DESC
                LIMIT 1
                """;

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

    // ==========================
    // INCREASE OTP ATTEMPT
    // ==========================
    public void increaseAttempt(Long otpId){

        String sql = """
                UPDATE user_otps
                SET attempt_count = attempt_count + 1
                WHERE id = ?
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setLong(1, otpId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==========================
    // MARK OTP USED
    // ==========================
    public void markUsed(Long otpId) {

        String sql = """
                UPDATE user_otps
                SET is_used = TRUE
                WHERE id = ?
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setLong(1, otpId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==========================
    // DELETE ALL OTP BY AUTH ID
    // ==========================
    public void deleteByAuthId(Long authId){

        String sql = """
                DELETE FROM user_otps
                WHERE auth_id = ?
                """;

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setLong(1, authId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==========================
    // MAP ROW
    // ==========================
    private UserOtp mapRow(ResultSet rs) throws SQLException {

        return new UserOtp(
                rs.getLong("id"),
                rs.getLong("auth_id"),
                rs.getString("otp_code"),
                rs.getTimestamp("expired_at").toLocalDateTime(),
                rs.getInt("attempt_count"),
                rs.getBoolean("is_used"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}