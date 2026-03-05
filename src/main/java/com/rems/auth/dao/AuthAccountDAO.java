package com.rems.auth.dao;

import com.rems.auth.model.AuthAccount;
import com.rems.common.constant.AccountStatus;

import java.sql.Connection;
import java.time.LocalDateTime;

public interface AuthAccountDAO {

    AuthAccount findByUserName(Connection conn, String username);
    AuthAccount findByEmail(Connection conn, String email);
    AuthAccount findById(Connection conn, Long id);

    Long save(Connection conn, AuthAccount account);

    void updateStatus(Connection conn, Long authId, AccountStatus status);

    void increaseLoginAttempt(Connection conn, Long authId, LocalDateTime now);

    void resetLoginAttempt(Connection conn, Long authId);
}
