package com.rems.notification.template;

public class TransactionCreatedEmail {

    public static String transactionCreated(String name,
                                            String propertyTitle,
                                            String transactionType,
                                            String amount) {
        return """
    <html>
    <body style="font-family: Arial">
        <h2 style="color:#2c3e50">REMS - Giao dich moi</h2>
        <p>Xin chao <b>%s</b>,</p>
        <p>Giao dich <b>%s</b> cua ban da duoc tao.</p>
        <p><b>Bat dong san:</b> %s</p>
        <p><b>So tien:</b> %s</p>
        <p>Vui long theo doi trang thai trong tai khoan cua ban.</p>
    </body>
    </html>
    """.formatted(name, transactionType, propertyTitle, amount);
    }
}
