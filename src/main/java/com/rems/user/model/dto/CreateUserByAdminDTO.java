package com.rems.user.model.dto;

import com.rems.common.constant.AccountStatus;
import com.rems.common.constant.Role;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;

import java.util.regex.Pattern;

public class CreateUserByAdminDTO {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final String fullName;
    private final String email;
    private final String phoneNumber;
    private final String password;
    private final Role role;
    private final AccountStatus status;

    public CreateUserByAdminDTO(String fullName, String email, String phoneNumber, String password, Role role, AccountStatus status) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public void validate() {

        if (isBlank(fullName)) throw new BusinessException(ErrorCode.FULLNAME_REQUIRED);

        if (isBlank(email)) throw new BusinessException(ErrorCode.EMAIL_REQUIRED);

        if (!EMAIL_PATTERN.matcher(email).matches()) throw new BusinessException(ErrorCode.EMAIL_INVALID);

        if (isBlank(phoneNumber)) throw new BusinessException(ErrorCode.PHONE_REQUIRED);

        if (!phoneNumber.matches("\\d{9,15}")) throw new BusinessException(ErrorCode.PHONE_INVALID);

        if (isBlank(password)) throw new BusinessException(ErrorCode.PASSWORD_REQUIRED);

        if (password.length() < 6) throw new BusinessException(ErrorCode.PASSWORD_TOO_SHORT);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) {}
    public String getEmail() { return email; }
    public void setEmail(String email) {}
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) {}
    public String getPassword() { return password; }
    public void setPassword(String password) {}
    public Role getRole() {return role;}
    public void setRole(Role role) {}

    public AccountStatus getStatus() {return status;}
    public void setStatus(AccountStatus status) {}
}
