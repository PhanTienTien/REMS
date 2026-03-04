package com.rems.auth.dao;

import com.rems.auth.model.AuthAccount;
import com.rems.common.constant.AccountStatus;

import java.time.LocalDateTime;

public interface AuthAccountDAO {
    AuthAccount findByUserName(String username) ;
    AuthAccount findByEmail(String email) ;
    AuthAccount findById(Long id) ;
    Long save(AuthAccount account) ;
    void updateStatus(Long authId, AccountStatus status);
    void increaseLoginAttempt(Long authId, LocalDateTime now);
    void resetLoginAttempt(Long authId);
}
