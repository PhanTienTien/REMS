package com.rems.auth.service.impl;

import com.rems.auth.dao.AuthAccountDAO;
import com.rems.auth.dao.UserOtpDAO;
import com.rems.auth.model.AuthAccount;
import com.rems.auth.model.UserOtp;
import com.rems.auth.model.dto.RegisterDto;
import com.rems.auth.service.AuthService;
import com.rems.common.constant.AccountStatus;
import com.rems.common.constant.Role;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.transaction.TransactionManager;
import com.rems.common.util.OtpUtil;
import com.rems.common.util.PasswordUtil;
import com.rems.user.dao.UserDAO;
import com.rems.user.model.User;

import java.sql.Connection;
import java.time.Duration;
import java.time.LocalDateTime;

public class AuthServiceImpl implements AuthService {

    private final AuthAccountDAO authAccountDAO;
    private final UserDAO userDAO;
    private final UserOtpDAO userOtpDAO;
    private final TransactionManager txManager;

    private static final int MAX_LOGIN_ATTEMPT = 5;
    private static final int MAX_OTP_ATTEMPT = 5;
    private static final int LOGIN_COOLDOWN_SECONDS = 3;
    private static final int OTP_EXPIRE_MINUTES = 5;

    public AuthServiceImpl(
            AuthAccountDAO authAccountDAO,
            UserDAO userDAO,
            UserOtpDAO userOtpDAO,
            TransactionManager txManager) {

        this.authAccountDAO = authAccountDAO;
        this.userDAO = userDAO;
        this.userOtpDAO = userOtpDAO;
        this.txManager = txManager;
    }

    // ================= REGISTER =================

    @Override
    public void register(RegisterDto dto) {

        txManager.execute(conn -> {

            if (authAccountDAO.findByEmail(conn, dto.getEmail()) != null) {
                throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }

            AuthAccount account = AuthAccount.local(
                    dto.getEmail(),
                    PasswordUtil.hash(dto.getPassword())
            );

            Long authId = authAccountDAO.save(conn, account);

            User user = new User();
            user.setAuthId(authId);
            user.setFullName(dto.getFullName());
            user.setEmail(dto.getEmail());
            user.setPhone_number(dto.getPhoneNumber());
            user.setRole(Role.CUSTOMER);
            user.setVerified(false);
            user.setDeleted(false);

            userDAO.save(conn, user);

            UserOtp otp = new UserOtp(
                    authId,
                    OtpUtil.generateOtp(),
                    LocalDateTime.now().plusMinutes(OTP_EXPIRE_MINUTES)
            );

            userOtpDAO.save(conn, otp);

            return null;
        });
    }

    // ================= LOGIN =================

    @Override
    public AuthAccount login(String username, String password) {

        return txManager.execute(conn -> {

            AuthAccount account =
                    authAccountDAO.findByUserName(conn, username);

            validateLogin(account);
            checkCooldown(account);

            if (!PasswordUtil.matches(password, account.getPasswordHash())) {

                authAccountDAO.increaseLoginAttempt(
                        conn,
                        account.getId(),
                        LocalDateTime.now()
                );

                throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
            }

            authAccountDAO.resetLoginAttempt(conn, account.getId());

            return account;
        });
    }

    // ================= VERIFY OTP =================

    @Override
    public void verifyOtp(String email, String otpInput) {

        txManager.execute(conn -> {

            AuthAccount account =
                    authAccountDAO.findByEmail(conn, email);

            if (account == null) {
                throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
            }

            UserOtp otp =
                    userOtpDAO.findLatestByAuthId(conn, account.getId());

            validateOtp(otp);

            if (!otp.getOtpCode().equals(otpInput)) {

                userOtpDAO.increaseAttempt(conn, otp.getId());
                throw new BusinessException(ErrorCode.INVALID_OTP);
            }

            userOtpDAO.markUsed(conn, otp.getId());
            authAccountDAO.updateStatus(
                    conn,
                    account.getId(),
                    AccountStatus.ACTIVE
            );

            return null;
        });
    }

    // ================= RESEND OTP =================

    @Override
    public void resendOtp(String email) {

        txManager.execute(conn -> {

            AuthAccount account =
                    authAccountDAO.findByEmail(conn, email);

            if (account == null) {
                throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
            }

            if (AccountStatus.ACTIVE.equals(account.getStatus())) {
                throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_VERIFIED);
            }

            UserOtp latest =
                    userOtpDAO.findLatestByAuthId(conn, account.getId());

            if (latest != null) {
                validateResendOtp(latest);
            }

            String otpCode = OtpUtil.generateOtp();
            LocalDateTime expiredAt =
                    LocalDateTime.now().plusMinutes(OTP_EXPIRE_MINUTES);

            userOtpDAO.save(
                    conn,
                    new UserOtp(account.getId(), otpCode, expiredAt)
            );

            return null;
        });
    }

    // ================= PRIVATE HELPERS =================

    private void validateEmailNotExists(Connection conn, String email) {
        if (authAccountDAO.findByEmail(conn, email) != null) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    private AuthAccount getAccountByEmail(Connection conn, String email) {

        AuthAccount account =
                authAccountDAO.findByEmail(conn, email);

        if (account == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        return account;
    }

    private void validateLogin(AuthAccount account) {

        if (account == null)
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);

        if (AccountStatus.LOCKED.equals(account.getStatus()))
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED);

        if (!AccountStatus.ACTIVE.equals(account.getStatus()))
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_ACTIVE);
    }

    private void handleFailedLogin(Connection conn, AuthAccount account) {

        LocalDateTime now = LocalDateTime.now();

        authAccountDAO.increaseLoginAttempt(conn, account.getId(), now);

        if (account.getLoginAttempt() + 1 >= MAX_LOGIN_ATTEMPT) {
            authAccountDAO.updateStatus(conn, account.getId(), AccountStatus.LOCKED);
        }
    }

    private void checkCooldown(AuthAccount account) {

        if (account.getLastLoginAttempt() == null) return;

        long seconds = Duration.between(
                account.getLastLoginAttempt(),
                LocalDateTime.now()
        ).getSeconds();

        if (seconds < LOGIN_COOLDOWN_SECONDS) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }

    private void validateOtp(UserOtp otp) {

        if (otp == null)
            throw new BusinessException(ErrorCode.OTP_NOT_FOUND);

        if (otp.isUsed())
            throw new BusinessException(ErrorCode.OTP_ALREADY_USED);

        if (otp.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new BusinessException(ErrorCode.OTP_EXPIRED);

        if (otp.getAttemptCount() >= MAX_OTP_ATTEMPT)
            throw new BusinessException(ErrorCode.OTP_TOO_MANY_ATTEMPTS);
    }

    private void validateResendOtp(UserOtp latest) {

        if (!latest.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.OTP_ALREADY_EXISTS);
        }

        if (latest.getAttemptCount() >= MAX_OTP_ATTEMPT) {
            throw new BusinessException(ErrorCode.OTP_TOO_MANY_ATTEMPTS);
        }
    }
}