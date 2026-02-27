package com.rems.user.service;

import com.rems.common.constant.AccountStatus;
import com.rems.common.constant.Role;
import com.rems.common.exception.BusinessException;
import com.rems.common.util.DBUtil;
import com.rems.common.util.EmailUtil;
import com.rems.common.util.OtpUtil;
import com.rems.common.util.PasswordUtil;
import com.rems.user.dao.UserDAO;
import com.rems.user.dao.UserOtpDAO;
import com.rems.user.model.User;
import com.rems.user.model.UserOtp;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

public class AuthService {

    UserDAO userDAO;
    UserOtpDAO userOtpDAO;

    private static final int MAX_LOGIN_ATTEMPT = 5;
    private static final int MAX_OTP_ATTEMPT = 5;
    private static final int LOGIN_WINDOW_MINUTES = 1;
    private static final int LOGIN_COOLDOWN_SECONDS = 3;
    private static final int OTP_EXPIRE_MINUTES = 5;

    public void register(String fullName, String email, String password) {

        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            UserDAO userDAO = new UserDAO(conn);
            UserOtpDAO otpDAO = new UserOtpDAO(conn);

            if (userDAO.findByEmail(email) != null) {
                throw new BusinessException("Email already exists");
            }

            String hashedPassword = PasswordUtil.hash(password);

            User user = new User(fullName, email, hashedPassword, Role.CUSTOMER);
            userDAO.save(user);

            String otpCode = OtpUtil.generateOtp();
            LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(OTP_EXPIRE_MINUTES);

            UserOtp otp = new UserOtp(user.getId(), otpCode, expiredAt);
            otpDAO.save(otp);

            conn.commit();

        } catch (Exception e) {
            rollback(conn);
            throw wrapException(e, "Register failed");
        } finally {
            close(conn);
        }
    }

    public void verifyOtp(String email, String otpInput) {

        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            UserDAO userDAO = new UserDAO(conn);
            UserOtpDAO otpDAO = new UserOtpDAO(conn);

            User user = userDAO.findByEmail(email);
            if (user == null) {
                throw new BusinessException("User not found");
            }

            UserOtp otp = otpDAO.findLatestByUserId(user.getId());
            validateOtp(otp);

            if (!otp.getOtpCode().equals(otpInput)) {

                otpDAO.increaseAttempt(otp.getId());

                conn.commit();
                throw new BusinessException("Invalid OTP");
            }

            otpDAO.markUsed(otp.getId());
            userDAO.updateStatus(user.getId(), AccountStatus.ACTIVE);

            conn.commit();

        } catch (Exception e) {
            rollback(conn);
            throw wrapException(e, "OTP verification failed");
        } finally {
            close(conn);
        }
    }

    public User login(String email, String password) {

        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            UserDAO userDAO = new UserDAO(conn);

            User user = userDAO.findByEmail(email);
            validateLoginUser(user);

            LocalDateTime now = LocalDateTime.now();

            checkCooldown(user, now);

            resetLoginWindowIfNeeded(userDAO, user, now);

            if (!PasswordUtil.matches(password, user.getPassword())) {

                int newAttempt = user.getLoginAttempt() + 1;

                userDAO.increaseLoginAttempt(user.getId(), now);

                if (newAttempt >= MAX_LOGIN_ATTEMPT) {
                    userDAO.updateStatus(user.getId(), AccountStatus.LOCKED);
                }

                conn.commit();
                throw new BusinessException("Invalid credentials");
            }

            userDAO.resetLoginSuccess(user.getId());

            conn.commit();
            return user;

        } catch (Exception e) {
            rollback(conn);
            throw wrapException(e, "Login failed");
        } finally {
            close(conn);
        }
    }

    public void resendOtp(String email) throws SQLException {

        User user = userDAO.findByEmail(email);

        if (user == null) {
            throw new BusinessException("Email không tồn tại");
        }

        if (AccountStatus.ACTIVE.equals(user.getStatus())) {
            throw new BusinessException("Tài khoản đã xác thực");
        }

        String otp = OtpUtil.generateOtp();

        UserOtp userOtp = new UserOtp();
        userOtp.setUserId(user.getId());
        userOtp.setOtpCode(otp);
        userOtp.setExpiredAt(LocalDateTime.now().plusMinutes(5));

        userOtpDAO.deleteByUserId(user.getId());
        userOtpDAO.insert(userOtp);

        EmailUtil.sendOtp(email, otp);
    }

    private void validateOtp(UserOtp otp) {
        if (otp == null)
            throw new BusinessException("OTP not found");

        if (otp.isUsed())
            throw new BusinessException("OTP already used");

        if (otp.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new BusinessException("OTP expired");

        if (otp.getAttemptCount() >= MAX_OTP_ATTEMPT)
            throw new BusinessException("Too many wrong attempts");
    }

    private void validateLoginUser(User user) {
        if (user == null)
            throw new BusinessException("Invalid credentials");

        if (user.getStatus() == AccountStatus.LOCKED)
            throw new BusinessException("Account locked");

        if (user.getStatus() != AccountStatus.ACTIVE)
            throw new BusinessException("Account not active");
    }

    private void checkCooldown(User user, LocalDateTime now) {
        if (user.getLastLoginAttempt() != null) {
            long seconds = Duration.between(user.getLastLoginAttempt(), now).getSeconds();
            if (seconds < LOGIN_COOLDOWN_SECONDS)
                throw new BusinessException("Too many requests. Please wait.");
        }
    }

    private void resetLoginWindowIfNeeded(UserDAO userDAO, User user, LocalDateTime now) {
        if (user.getLoginAttemptWindowStart() == null ||
                Duration.between(user.getLoginAttemptWindowStart(), now).toMinutes() >= LOGIN_WINDOW_MINUTES) {

            userDAO.resetLoginWindow(user.getId(), now);
            user.setLoginAttempt(0);
        }
    }

    private void rollback(Connection conn) {
        try {
            if (conn != null) conn.rollback();
        } catch (Exception ignored) {}
    }

    private void close(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (Exception ignored) {}
    }

    private RuntimeException wrapException(Exception e, String message) {
        if (e instanceof BusinessException) {
            return (BusinessException) e;
        }
        return new RuntimeException(message, e);
    }
}