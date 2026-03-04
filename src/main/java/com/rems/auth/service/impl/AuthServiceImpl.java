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

        txManager.execute(() -> {

            validateEmailNotExists(dto.getEmail());

            AuthAccount account = AuthAccount.local(
                    dto.getEmail(),
                    PasswordUtil.hash(dto.getPassword())
            );

            Long authId = authAccountDAO.save(account);

            User user = new User();
            user.setAuthId(authId);
            user.setFullName(dto.getFullName());
            user.setEmail(dto.getEmail());
            user.setPhone_number(dto.getPhoneNumber());
            user.setRole(Role.CUSTOMER);
            user.setVerified(false);

            userDAO.save(user);

            String otpCode = OtpUtil.generateOtp();
            LocalDateTime expiredAt =
                    LocalDateTime.now().plusMinutes(OTP_EXPIRE_MINUTES);

            UserOtp otp = new UserOtp(authId, otpCode, expiredAt);

            userOtpDAO.save(otp);
        });
    }

    // ================= LOGIN =================

    @Override
    public AuthAccount login(String username, String password) {

        return txManager.executeWithResult(() -> {

            AuthAccount account = authAccountDAO.findByUserName(username);

            validateLogin(account);
            checkCooldown(account);

            if (!PasswordUtil.matches(password, account.getPasswordHash())) {

                handleFailedLogin(account);

                throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
            }

            authAccountDAO.resetLoginAttempt(account.getId());
            return account;
        });
    }

    // ================= VERIFY OTP =================

    @Override
    public void verifyOtp(String email, String otpInput) {

        txManager.execute(() -> {

            AuthAccount account = getAccountByEmail(email);

            UserOtp otp = userOtpDAO.findLatestByAuthId(account.getId());
            validateOtp(otp);

            if (!otp.getOtpCode().equals(otpInput)) {

                userOtpDAO.increaseAttempt(otp.getId());
                throw new BusinessException(ErrorCode.INVALID_OTP);
            }

            userOtpDAO.markUsed(otp.getId());
            authAccountDAO.updateStatus(account.getId(), AccountStatus.ACTIVE);
        });
    }

    // ================= RESEND OTP =================

    @Override
    public void resendOtp(String email) {

        txManager.execute(() -> {

            AuthAccount account = getAccountByEmail(email);

            if (AccountStatus.ACTIVE.equals(account.getStatus())) {
                throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_VERIFIED);
            }

            UserOtp latest = userOtpDAO.findLatestByAuthId(account.getId());

            if (latest != null) {
                validateResendOtp(latest);
            }

            String otpCode = OtpUtil.generateOtp();
            LocalDateTime expiredAt =
                    LocalDateTime.now().plusMinutes(OTP_EXPIRE_MINUTES);

            userOtpDAO.save(new UserOtp(account.getId(), otpCode, expiredAt));
        });
    }

    // ================= PRIVATE HELPERS =================

    private void validateEmailNotExists(String email) {
        if (authAccountDAO.findByEmail(email) != null) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    private AuthAccount getAccountByEmail(String email) {
        AuthAccount account = authAccountDAO.findByEmail(email);
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

    private void handleFailedLogin(AuthAccount account) {

        LocalDateTime now = LocalDateTime.now();

        authAccountDAO.increaseLoginAttempt(account.getId(), now);

        if (account.getLoginAttempt() + 1 >= MAX_LOGIN_ATTEMPT) {
            authAccountDAO.updateStatus(account.getId(), AccountStatus.LOCKED);
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