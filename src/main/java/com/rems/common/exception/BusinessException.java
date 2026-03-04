package com.rems.common.exception;

public class BusinessException extends AppException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage(), "BUSINESS_ERROR");
    }
}