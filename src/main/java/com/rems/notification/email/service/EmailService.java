package com.rems.notification.email.service;

public interface EmailService {

    void sendOtpEmail(String to, String otp);

    void sendTransactionCreatedEmail(String to,
                                     String customerName,
                                     String propertyTitle,
                                     String transactionType,
                                     String amount);

    void sendTransactionCompletedEmail(String to,
                                       String customerName,
                                       String propertyTitle);

}
