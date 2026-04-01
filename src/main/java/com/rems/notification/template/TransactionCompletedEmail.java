package com.rems.notification.template;

public class TransactionCompletedEmail {
    public static String transactionCompleted(String name, String property) {

        return """
    <html>
    <body style="font-family: Arial">

        <h2 style="color:green">✅ Giao dịch hoàn tất</h2>

        <p>Xin chào <b>%s</b>,</p>

        <p>Bạn đã hoàn tất giao dịch cho:</p>

        <p><b>%s</b></p>

        <p>Cảm ơn bạn đã sử dụng hệ thống REMS.</p>

    </body>
    </html>
    """.formatted(name, property);
    }
}
