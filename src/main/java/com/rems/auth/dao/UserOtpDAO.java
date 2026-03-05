package com.rems.auth.dao;

import com.rems.auth.model.UserOtp;

import java.sql.Connection;

public interface UserOtpDAO {

    void save(Connection conn, UserOtp otp);

    UserOtp findLatestByAuthId(Connection conn, Long authId);

    void increaseAttempt(Connection conn, Long otpId);

    void markUsed(Connection conn, Long otpId);

    void deleteByAuthId(Connection conn, Long authId);
}
