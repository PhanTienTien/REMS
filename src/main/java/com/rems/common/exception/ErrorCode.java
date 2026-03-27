package com.rems.common.exception;

public enum ErrorCode {

    VALIDATION_ERROR("COMMON_001", "Validation error"),

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
    TRANSACTION_NOT_FOUND("AUTH_0019", "Transaction not found" ),
    PRICE_IS_NOT_VALID("AUTH_0020", "Price is not valid" ),
    PERMISSION_DENIED("AUTH_0021", "Permission denied" ),
    CHECK_FAILED("AUTH_0022", "Check failed" ),
    INVALID_CREDENTIAL("AUTH_0023", "Invalid credential" ),
    ACCOUNT_NOT_ACTIVE("AUTH_0024", "Account not active" ),
    ACCOUNT_NOT_FOUND("AUTH_0025", "Account not found" ),
    INVALID_OTP("AUTH_0026", "Invalid OTP" ),
    ACCOUNT_ALREADY_VERIFIED("AUTH_0027", "Account already verified" ),
    OTP_ALREADY_EXISTS("AUTH_0028", "OTP already exists" ),
    OTP_ALREADY_USED("AUTH_0029", "OTP already used" ),
    TOO_MANY_REQUEST("AUTH_0030", "Too many requests" ),
    EMAIL_INVALID("AUTH_0031", "Email invalid" ),
    PHONE_REQUIRED("AUTH_0032", "Phone required" ),
    PHONE_INVALID("AUTH_0033", "Phone invalid" ),
    PASSWORD_TOO_SHORT("AUTH_0034", "Password too short" ),
    PASSWORD_NOT_MATCH("AUTH_0035", "Password not match" ),
    USERNAME_REQUIRED("AUTH_0036", "Username required" ),
    INVALID_INPUT("AUTH_0037", "Invalid input" ),
    NOT_FOUND("AUTH_0038", "User not found" ),
    INVALID_STATE_TRANSITION("AUTH_0039", "Invalid state transition" ),
    PROPERTY_NOT_AVAILABLE("AUTH_0040", "Property not available" ),
    TYPE_MISMATCH("AUTH_0041", "Type mismatch" ),
    CANNOT_DEACTIVATE("AUTH_0042", "Cannot deactivate" ),
    NEVER_APPROVED("AUTH_0043", "Never approved" ),
    BOOKING_ALREADY_ACCEPTED("AUTH_0044", "Booking already accepted" ),
    FORBIDDEN("AUTH_0045", "Forbidden" ),
    INVALID_STATE("AUTH_0046", "Invalid state" ),
    DUPLICATE_TRANSACTION("AUTH_0047", "Duplicate transaction" ),
    INVALID_PASSWORD("AUTH_0048", "Invalid password" ),
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}