package com.rems.auth.service;

import com.rems.auth.model.dto.RegisterDto;
import com.rems.auth.service.impl.AuthServiceImpl;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthServiceImpl authService;

    @Test
    public void testRegisterWithValidData() {
        // Arrange
        RegisterDto validDto = new RegisterDto(
                "Test User",
                "test@example.com",
                "1234567890",
                "password123",
                "password123"
        );

        // Act & Assert
        assertDoesNotThrow(() -> {
            validDto.validate();
        });
    }

    @Test
    public void testRegisterWithInvalidEmail() {
        // Arrange
        RegisterDto invalidDto = new RegisterDto(
                "Test User",
                "invalid-email",
                "1234567890",
                "password123",
                "password123"
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            invalidDto.validate();
        });
        assertEquals(ErrorCode.EMAIL_INVALID, exception.getErrorCode());
    }

    @Test
    public void testRegisterWithPasswordMismatch() {
        // Arrange
        RegisterDto invalidDto = new RegisterDto(
                "Test User",
                "test@example.com",
                "1234567890",
                "password123",
                "differentpassword"
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            invalidDto.validate();
        });
        assertEquals(ErrorCode.PASSWORD_NOT_MATCH, exception.getErrorCode());
    }

    @Test
    public void testRegisterWithShortPassword() {
        // Arrange
        RegisterDto invalidDto = new RegisterDto(
                "Test User",
                "test@example.com",
                "1234567890",
                "123",
                "123"
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            invalidDto.validate();
        });
        assertEquals(ErrorCode.PASSWORD_TOO_SHORT, exception.getErrorCode());
    }
}
