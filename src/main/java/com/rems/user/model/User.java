package com.rems.user.model;

import com.rems.common.constant.Role;

import java.time.LocalDateTime;

public class User {

    private Long id;
    private Long authId;   // NEW
    private String fullName;
    private String email;
    private String phone_number;
    private Role role;
    private boolean isVerified;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(long id, long authId, String fullName, String email, String phone_number, Role role, boolean isVerified, boolean isDeleted, LocalDateTime createdAt, LocalDateTime localDateTime) {
        this.id = id;
        this.authId = authId;
        this.fullName = fullName;
        this.email = email;
        this.phone_number = phone_number;
        this.role = role;
        this.isVerified = isVerified;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = localDateTime;
    }

    public User() {

    }

    public long getAuthId() {
        return authId;
    }

    public void setAuthId(long authId) {
        this.authId = authId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public boolean isVerified() {
        return isVerified;
    }
    public void setVerified(boolean verified) {
        isVerified = verified;
    }
    public boolean isDeleted() {
        return isDeleted;
    }
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}