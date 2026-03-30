package com.rems.common.exception;

public enum ErrorCode {

    VALIDATION_ERROR("COMMON_001", "Dữ liệu không hợp lệ"),
    INVALID_INPUT("COMMON_002", "Dữ liệu đầu vào không hợp lệ"),
    NOT_FOUND("COMMON_003", "Không tìm thấy dữ liệu"),
    CHECK_FAILED("COMMON_004", "Kiểm tra dữ liệu thất bại"),
    PERMISSION_DENIED("COMMON_005", "Bạn không có quyền thực hiện thao tác này"),
    FORBIDDEN("COMMON_006", "Truy cập bị từ chối"),
    TOO_MANY_REQUEST("COMMON_007", "Bạn thao tác quá nhanh, vui lòng thử lại sau"),

    EMAIL_ALREADY_EXISTS("AUTH_001", "Email đã tồn tại trong hệ thống"),
    USER_NOT_FOUND("USER_001", "Không tìm thấy người dùng"),
    INVALID_CREDENTIALS("AUTH_002", "Email hoặc mật khẩu không đúng"),
    INVALID_CREDENTIAL("AUTH_003", "Thông tin đăng nhập không hợp lệ"),
    ACCOUNT_LOCKED("AUTH_004", "Tài khoản đã bị khóa"),
    ACCOUNT_NOT_VERIFIED("AUTH_005", "Tài khoản chưa được xác minh"),
    ACCOUNT_NOT_ACTIVE("AUTH_006", "Tài khoản chưa được kích hoạt"),
    ACCOUNT_NOT_FOUND("AUTH_007", "Không tìm thấy tài khoản"),
    ACCOUNT_ALREADY_VERIFIED("AUTH_008", "Tài khoản đã được xác minh trước đó"),
    INVALID_ACCOUNT_STATE("AUTH_009", "Trạng thái tài khoản không hợp lệ"),

    OTP_EXPIRED("AUTH_010", "Mã OTP đã hết hạn"),
    OTP_INVALID("AUTH_011", "Mã OTP không hợp lệ"),
    INVALID_OTP("AUTH_012", "Mã OTP không hợp lệ"),
    OTP_NOT_FOUND("AUTH_013", "Không tìm thấy mã OTP"),
    OTP_ALREADY_EXISTS("AUTH_014", "OTP hiện tại vẫn còn hiệu lực, chưa thể gửi lại"),
    OTP_ALREADY_USED("AUTH_015", "Mã OTP đã được sử dụng"),
    OTP_TOO_MANY_ATTEMPTS("AUTH_016", "Bạn đã nhập sai OTP quá nhiều lần"),
    OTP_LIMIT_REACHED("AUTH_017", "Bạn đã vượt quá số lần gửi OTP cho phép"),

    EMAIL_REQUIRED("AUTH_018", "Vui lòng nhập email"),
    EMAIL_INVALID("AUTH_019", "Email không đúng định dạng"),
    PHONE_REQUIRED("AUTH_020", "Vui lòng nhập số điện thoại"),
    PHONE_INVALID("AUTH_021", "Số điện thoại không đúng định dạng"),
    FULLNAME_REQUIRED("AUTH_022", "Vui lòng nhập họ và tên"),
    USERNAME_REQUIRED("AUTH_023", "Vui lòng nhập tên đăng nhập"),
    PASSWORD_REQUIRED("AUTH_024", "Vui lòng nhập mật khẩu"),
    PASSWORD_TOO_SHORT("AUTH_025", "Mật khẩu phải có ít nhất 6 ký tự"),
    PASSWORD_NOT_MATCH("AUTH_026", "Mật khẩu xác nhận không khớp"),
    PASSWORD_INCORRECT("AUTH_027", "Mật khẩu hiện tại không đúng"),
    INVALID_PASSWORD("AUTH_028", "Mật khẩu không đúng"),

    PROPERTY_NOT_FOUND("PROPERTY_001", "Không tìm thấy bất động sản"),
    PROPERTY_NOT_AVAILABLE("PROPERTY_002", "Bất động sản hiện không khả dụng"),
    INVALID_STATE_TRANSITION("PROPERTY_003", "Không thể chuyển trạng thái bất động sản theo yêu cầu"),
    CANNOT_DEACTIVATE("PROPERTY_004", "Không thể ẩn bất động sản ở trạng thái hiện tại"),
    NEVER_APPROVED("PROPERTY_005", "Bất động sản này chưa từng được duyệt"),
    TYPE_MISMATCH("PROPERTY_006", "Loại bất động sản không khớp với giao dịch"),
    PRICE_IS_NOT_VALID("PROPERTY_007", "Giá bất động sản không hợp lệ"),

    BOOKING_NOT_FOUND("BOOKING_001", "Không tìm thấy lịch hẹn"),
    BOOKING_ALREADY_ACCEPTED("BOOKING_002", "Lịch hẹn này đã được chấp nhận trước đó"),

    TRANSACTION_NOT_FOUND("TRANSACTION_001", "Không tìm thấy giao dịch"),
    DUPLICATE_TRANSACTION("TRANSACTION_002", "Lịch hẹn này đã có giao dịch"),

    INVALID_STATE("DOMAIN_001", "Trạng thái hiện tại không cho phép thực hiện thao tác này");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
