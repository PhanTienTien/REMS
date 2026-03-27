package com.rems.common.exception;

public class ValidationException extends AppException {

    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_ERROR);
    }
}
