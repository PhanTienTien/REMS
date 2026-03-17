package com.rems.auth.service;

import com.rems.auth.model.AuthAccount;
import com.rems.auth.model.dto.RegisterDto;

public interface AuthService {

    void register(RegisterDto dto);

    void verifyOtp(String email, String otp);

    AuthAccount login(String username, String password);

    void resendOtp(String email);

    void changePassword(Long userId,
                        String currentPassword,
                        String newPassword);

}
