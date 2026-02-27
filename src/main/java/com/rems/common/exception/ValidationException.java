package com.rems.common.exception;

public class ValidationException extends AppException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}
