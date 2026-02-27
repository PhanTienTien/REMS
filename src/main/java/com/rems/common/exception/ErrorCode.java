package com.rems.common.exception;

public enum ErrorCode {

    EMAIL_ALREADY_EXISTS("AUTH_001", "Email already exists"),
    USER_NOT_FOUND("AUTH_002", "User not found"),
    INVALID_CREDENTIALS("AUTH_003", "Invalid credentials"),
    ACCOUNT_LOCKED("AUTH_004", "Account locked"),
    ACCOUNT_NOT_VERIFIED("AUTH_005", "Account not verified"),
    OTP_EXPIRED("AUTH_006", "OTP expired"),
    OTP_INVALID("AUTH_007", "Invalid OTP"),
    OTP_TOO_MANY_ATTEMPTS("AUTH_008", "Too many wrong attempts. OTP invalidated."),
    OTP_LIMIT_REACHED("AUTH_009", "OTP limit reached for today"),
    INVALID_ACCOUNT_STATE("AUTH_0010", "Invalid account state" ),
    OTP_NOT_FOUND("AUTH_0011", "OTP not found" ),
    EMAIL_REQUIRED("AUTH_0012", "Email required" ),
    OTP_REQUIRED("AUTH_0013", "OTP required" ),
    FULLNAME_REQUIRED("AUTH_0014", "Fullname required" ),
    PASSWORD_REQUIRED("AUTH_0015", "Password must be at least 6 characters"),
    PASSWORD_INCORRECT("AUTH_0016", "Password incorrect" ),
    PROPERTY_NOT_FOUND("AUTH_0017", "Property not found" ),
    PROPERT_NOT_AVAILABLE("AUTH_0018", "Property not available" ),
    TRANSACTION_NOT_FOUND("AUTH_0019", "Transaction not found" ),
    PRICE_IS_NOT_VALID("AUTH_0020", "Price is not valid" ),
    PERMISSION_DENIED("AUTH_0021", "Permission denied" ),
    CHECK_FAILED("AUTH_0022", "Check failed" ),
    INVALID_CREDENTIAL("AUTH_0023", "Invalid credential" ),
    ACCOUNT_NOT_ACTIVE("AUTH_0024", "Account not active" ),;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}