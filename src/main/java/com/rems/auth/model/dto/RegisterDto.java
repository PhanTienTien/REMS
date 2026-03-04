package com.rems.auth.model.dto;

import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;

import java.util.regex.Pattern;

public class RegisterDto {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final String fullName;
    private final String email;
    private final String phoneNumber;
    private final String password;
    private final String confirmPassword;

    public RegisterDto(String fullName,
                       String email,
                       String phoneNumber,
                       String password,
                       String confirmPassword) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public void validate() {

        if (isBlank(fullName)) throw new BusinessException(ErrorCode.FULLNAME_REQUIRED);

        if (isBlank(email)) throw new BusinessException(ErrorCode.EMAIL_REQUIRED);

        if (!EMAIL_PATTERN.matcher(email).matches()) throw new BusinessException(ErrorCode.EMAIL_INVALID);

        if (isBlank(phoneNumber)) throw new BusinessException(ErrorCode.PHONE_REQUIRED);

        if (!phoneNumber.matches("\\d{9,15}")) throw new BusinessException(ErrorCode.PHONE_INVALID);

        if (isBlank(password)) throw new BusinessException(ErrorCode.PASSWORD_REQUIRED);

        if (password.length() < 6) throw new BusinessException(ErrorCode.PASSWORD_TOO_SHORT);

        if (!password.equals(confirmPassword)) throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
}