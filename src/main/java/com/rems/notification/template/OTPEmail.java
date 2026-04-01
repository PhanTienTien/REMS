package com.rems.notification.template;

public class OTPEmail {
    public static String otp(String otp) {
        return """
    <html>
    <body>
        <h2>Xác thực tài khoản</h2>
        <p>Mã OTP của bạn:</p>
        <h1>%s</h1>
        <p>Hết hạn sau 5 phút</p>
    </body>
    </html>
    """.formatted(otp);
    }
}
