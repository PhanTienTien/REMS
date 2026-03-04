package com.rems.auth.dao;

import com.rems.auth.model.UserOtp;

public interface UserOtpDAO {
    void save(UserOtp otp);
    UserOtp findLatestByAuthId(Long authId);
    void increaseAttempt(Long otpId);
    void markUsed(Long otpId);
    void deleteByAuthId(Long authId);
}
