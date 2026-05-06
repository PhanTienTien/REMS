package com.rems.notification.email.service.impl;

import com.rems.common.util.DBUtil;
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
        String host = getConfigProperty("email.smtp.host", "smtp.gmail.com");
        String port = getConfigProperty("email.smtp.port", "587");
        String username = getConfigProperty("email.username", null);
        String password = getConfigProperty("email.password", null);
        String from = getConfigProperty("email.from", username);
        boolean auth = Boolean.parseBoolean(getConfigProperty("email.smtp.auth", "true"));
        boolean startTls = Boolean.parseBoolean(getConfigProperty("email.smtp.starttls", "true"));

        if (username == null || password == null || from == null) {
            System.err.println("[MAIL] Skip sending email due to missing config (email.username/email.password/email.from)");
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

    private String getConfigProperty(String key, String defaultValue) {
        String value = DBUtil.getConfigProperty(key);
        return value != null && !value.isBlank() ? value : defaultValue;
    }
}
