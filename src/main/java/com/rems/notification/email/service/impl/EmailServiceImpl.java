package com.rems.notification.email.service.impl;

import com.rems.notification.email.service.EmailService;
import com.rems.notification.template.OTPEmail;
import com.rems.notification.template.TransactionCompletedEmail;
import com.rems.notification.template.TransactionCreatedEmail;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailServiceImpl implements EmailService {

    @Override
    public void sendOtpEmail(String to, String otp) {

        sendHtmlEmail(
                to,
                "REMS - Xác thực tài khoản",
                OTPEmail.otp(otp)
        );
    }

    @Override
    public void sendTransactionCreatedEmail(String to,
                                            String customerName,
                                            String propertyTitle,
                                            String transactionType,
                                            String amount) {
        sendHtmlEmail(
                to,
                "REMS - Giao dich da duoc tao",
                TransactionCreatedEmail.transactionCreated(
                        customerName,
                        propertyTitle,
                        transactionType,
                        amount
                )
        );
    }

    @Override
    public void sendTransactionCompletedEmail(String to,
                                              String name,
                                              String propertyTitle) {

        sendHtmlEmail(
                to,
                "REMS - Giao dịch hoàn tất",
                TransactionCompletedEmail.transactionCompleted(name, propertyTitle)
        );
    }

    private void sendHtmlEmail(String to, String subject, String html) {
        String host = getConfig("MAIL_HOST", "smtp.gmail.com");
        String port = getConfig("MAIL_PORT", "587");
        String username = getConfig("MAIL_USERNAME", null);
        String password = getConfig("MAIL_PASSWORD", null);
        String from = getConfig("MAIL_FROM", username);
        boolean auth = Boolean.parseBoolean(getConfig("MAIL_AUTH", "true"));
        boolean startTls = Boolean.parseBoolean(getConfig("MAIL_STARTTLS", "true"));

        if (username == null || password == null || from == null) {
            System.err.println("[MAIL] Skip sending email due to missing config (MAIL_USERNAME/MAIL_PASSWORD/MAIL_FROM)");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", String.valueOf(auth));
        props.put("mail.smtp.starttls.enable", String.valueOf(startTls));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(html, "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (Exception e) {
            System.err.println("[MAIL] Failed to send email: " + e.getMessage());
        }
    }

    private String getConfig(String key, String defaultValue) {
        String fromEnv = System.getenv(key);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        String fromSystem = System.getProperty(key);
        if (fromSystem != null && !fromSystem.isBlank()) {
            return fromSystem;
        }
        return defaultValue;
    }
}
