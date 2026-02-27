package com.rems;

import com.rems.user.model.User;
import com.rems.user.service.AuthService;
import com.rems.common.exception.BusinessException;

public class AuthTest {

    public static void main(String[] args) {

        AuthService authService = new AuthService();

        try {

            // =========================
            // 1. REGISTER
            // =========================
//            System.out.println("=== REGISTER ===");
//            authService.register(
//                    "Test User",
//                    "test5@gmail.com",
//                    "123456"
//            );
//            System.out.println("Register success!");

            // =========================
            // 2. VERIFY OTP (nhập tay)
            // =========================
//            System.out.println("=== VERIFY OTP ===");
//
//            // ⚠ Vào DB lấy OTP code rồi dán vào đây
//            String otp = "976071"; // sửa lại đúng OTP
//
//            authService.verifyOtp("test5@gmail.com", otp);
//            System.out.println("OTP verified!");

            // =========================
            // 3. LOGIN
            // =========================
            System.out.println("=== LOGIN ===");

            User user = authService.login("test5@gmail.com", "1");
            System.out.println("Login success: " + user.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
