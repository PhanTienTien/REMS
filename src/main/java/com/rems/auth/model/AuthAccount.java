package com.rems.auth.model;

import com.rems.common.constant.AccountStatus;
import java.time.LocalDateTime;

public class AuthAccount {

    private Long id;
    private String userName;
    private String email;
    private String phone_number;
    private String passwordHash;
    private String provider;
    private String providerId;
    private AccountStatus status;
    private int loginAttempt;
    private LocalDateTime lastLoginAttempt;
    private LocalDateTime createdAt;


    public AuthAccount() {}

    public AuthAccount(String userName, String passwordHash) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.provider = "LOCAL";
        this.status = AccountStatus.ACTIVE;
        this.loginAttempt = 0;
    }

    public AuthAccount(Long id,
                       String userName,
                       String email,
                       String phoneNumber,
                       String passwordHash,
                       String provider,
                       String providerId,
                       AccountStatus status,
                       int loginAttempt,
                       LocalDateTime lastLoginAttempt,
                       LocalDateTime createdAt) {

        this.id = id;
        this.userName = userName;
        this.email = email;
        this.phone_number = phoneNumber;
        this.passwordHash = passwordHash;
        this.provider = provider;
        this.providerId = providerId;
        this.status = status;
        this.loginAttempt = loginAttempt;
        this.lastLoginAttempt = lastLoginAttempt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public String getProvider() {
        return provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public String getProviderId() {
        return providerId;
    }
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
    public AccountStatus getStatus() {
        return status;
    }
    public void setStatus(AccountStatus status) {
        this.status = status;
    }
    public Integer getLoginAttempt() {
        return loginAttempt;
    }
    public void setLoginAttempt(Integer loginAttempt) {
        this.loginAttempt = loginAttempt;
    }
    public LocalDateTime getLastLoginAttempt() {
        return lastLoginAttempt;
    }
    public void setLastLoginAttempt(LocalDateTime lastLoginAttempt) {
        this.lastLoginAttempt = lastLoginAttempt;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static AuthAccount local(String email,
                                    String passwordHash) {

        AuthAccount account = new AuthAccount();

        account.setUserName(email);
        account.setEmail(email);
        account.setPasswordHash(passwordHash);
        account.setProvider("LOCAL");
        account.setStatus(AccountStatus.PENDING);
        account.setLoginAttempt(0);
        account.setCreatedAt(LocalDateTime.now());

        return account;
    }
}
