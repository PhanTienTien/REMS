package com.rems.common.exception;

public class BusinessException extends AppException {

    public BusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}