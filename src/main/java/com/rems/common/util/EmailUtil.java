package com.rems.common.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailUtil {

    private static final String FROM_EMAIL = "your-email@gmail.com";
    private static final String APP_PASSWORD = "your-app-password";

    public static void sendOtp(String email, String Otp) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );

            message.setSubject("Verify Your Account - REMS");

            String htmlContent = """
                    <div style="font-family:Arial,sans-serif">
                        <h2>Verify Your Account</h2>
                        <p>Your OTP code is:</p>
                        <h1 style="color:#2e86de;">%s</h1>
                        <p>This code will expire in <b>5 minutes</b>.</p>
                        <p>If you did not request this, please ignore this email.</p>
                        <br>
                        <small>REMS System</small>
                    </div>
                    """.formatted(Otp);

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}
