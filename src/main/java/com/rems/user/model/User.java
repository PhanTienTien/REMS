package com.rems.user.model;

import com.rems.common.constant.AccountStatus;
import com.rems.common.constant.Role;

import java.time.LocalDateTime;

public class User {

    private Long id;
    private String fullName;
    private String email;
    private String password;
    private Role role;
    private AccountStatus status;
    private Boolean isVerified;
    private Integer loginAttempt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAttempt;
    private LocalDateTime loginAttemptWindowStart;

    // ===== Constructor rỗng =====
    public User() {
    }

    // ===== Constructor dùng khi tạo mới =====
    public User(String fullName, String email, String password, Role role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = AccountStatus.PENDING;
        this.isVerified = false;
        this.loginAttempt = 0;
    }

    // ===== Constructor đầy đủ =====
    public User(Long id,
                String fullName,
                String email,
                String password,
                Role role,
                AccountStatus status,
                Boolean isVerified,
                Integer loginAttempt,
                LocalDateTime lastLoginAttempt,
                LocalDateTime loginAttemptWindowStart,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.isVerified = isVerified;
        this.loginAttempt = loginAttempt;
        this.lastLoginAttempt = lastLoginAttempt;
        this.loginAttemptWindowStart = loginAttemptWindowStart;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ===== Getter & Setter =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public Boolean getVerified() { return isVerified; }
    public void setVerified(Boolean verified) { isVerified = verified; }

    public Integer getLoginAttempt() { return loginAttempt; }
    public void setLoginAttempt(Integer loginAttempt) { this.loginAttempt = loginAttempt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getLastLoginAttempt() { return lastLoginAttempt; }
    public LocalDateTime getLoginAttemptWindowStart() { return loginAttemptWindowStart; }

    // ===== toString (không in password) =====
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", isVerified=" + isVerified +
                ", loginAttempt=" + loginAttempt +
                ", createdAt=" + createdAt +
                '}';
    }
}