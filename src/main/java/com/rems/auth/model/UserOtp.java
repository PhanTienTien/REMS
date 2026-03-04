package com.rems.auth.model;

import java.time.LocalDateTime;

public class UserOtp {

    private Long id;
    private Long authId;
    private String otpCode;
    private LocalDateTime expiredAt;
    private Integer attemptCount;
    private Boolean isUsed;
    private LocalDateTime createdAt;

    // Constructor rỗng
    public UserOtp() {
    }

    // Constructor tạo mới OTP
    public UserOtp(Long authId, String otpCode, LocalDateTime expiredAt) {
        this.authId = authId;
        this.otpCode = otpCode;
        this.expiredAt = expiredAt;
        this.attemptCount = 0;
        this.isUsed = false;
    }

    // Constructor đầy đủ
    public UserOtp(Long id, Long authId, String otpCode,
                   LocalDateTime expiredAt, Integer attemptCount,
                   Boolean isUsed, LocalDateTime createdAt) {

        this.id = id;
        this.authId = authId;
        this.otpCode = otpCode;
        this.expiredAt = expiredAt;
        this.attemptCount = attemptCount;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
    }

    // ===== Getter & Setter =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return authId; }
    public void setUserId(Long userId) { this.authId = userId; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public LocalDateTime getExpiredAt() { return expiredAt; }
    public void setExpiredAt(LocalDateTime expiredAt) { this.expiredAt = expiredAt; }

    public Integer getAttemptCount() { return attemptCount; }
    public void setAttemptCount(Integer attemptCount) { this.attemptCount = attemptCount; }

    public Boolean getUsed() { return isUsed; }
    public void setUsed(Boolean used) { isUsed = used; }

    public Boolean isUsed() { return isUsed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "UserOtp{" +
                "id=" + id +
                ", userId=" + authId +
                ", otpCode='" + otpCode + '\'' +
                ", expiredAt=" + expiredAt +
                ", attemptCount=" + attemptCount +
                ", isUsed=" + isUsed +
                '}';
    }

    public long getAuthId() {
        return authId;
    }

    public void setAuthId(long authId) {
        this.authId = authId;
    }
}