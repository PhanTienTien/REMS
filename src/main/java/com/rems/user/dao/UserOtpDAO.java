package com.rems.user.dao;

import com.rems.common.base.BaseDAO;
import com.rems.common.util.DBUtil;
import com.rems.user.model.UserOtp;

import java.sql.*;
import java.time.LocalDateTime;

public class UserOtpDAO extends BaseDAO {

    public UserOtpDAO(Connection connection) {
        super(connection);
    }

    public void save(UserOtp otp) throws SQLException {

        String sql = """
                INSERT INTO user_otps
                (user_id, otp_code, expired_at, attempt_count, is_used)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, otp.getUserId());
            ps.setString(2, otp.getOtpCode());
            ps.setTimestamp(3, Timestamp.valueOf(otp.getExpiredAt()));
            ps.setInt(4, otp.getAttemptCount());
            ps.setBoolean(5, otp.getUsed());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    otp.setId(rs.getLong(1));
                }
            }
        }
    }

    public UserOtp findValidOtp(Long userId, String code) throws SQLException {

        String sql = """
                SELECT * FROM user_otps
                WHERE user_id = ?
                  AND otp_code = ?
                  AND is_used = FALSE
                ORDER BY created_at DESC
                LIMIT 1
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setString(2, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }

        return null;
    }

    public void increaseAttempt(Long otpId) {

        String sql = """
        UPDATE user_otps
        SET attempt_count = attempt_count + 1
        WHERE id = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, otpId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error increasing OTP attempt", e);
        }
    }

    public void markUsed(Long otpId) {

        String sql = """
        UPDATE user_otps
        SET is_used = TRUE
        WHERE id = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, otpId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error marking OTP used", e);
        }
    }

    private UserOtp mapRow(ResultSet rs) throws SQLException {

        return new UserOtp(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("otp_code"),
                rs.getTimestamp("expired_at").toLocalDateTime(),
                rs.getInt("attempt_count"),
                rs.getBoolean("is_used"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    public UserOtp findLatestByUserId(Long userId) {

        String sql = """
        SELECT *
        FROM user_otps
        WHERE user_id = ?
        ORDER BY created_at DESC
        LIMIT 1
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding latest OTP", e);
        }
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_otp WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot delete OTP");
        }
    }

    public void insert(UserOtp userOtp) {
        String sql = """
        INSERT INTO user_otp (user_id, otp, expired_at)
        VALUES (?, ?, ?)
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userOtp.getUserId());
            ps.setString(2, userOtp.getOtpCode());
            ps.setTimestamp(3, Timestamp.valueOf(userOtp.getExpiredAt()));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot insert OTP");
        }
    }
}