package com.rems.common.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testHashPassword() {
        // Arrange
        String password = "testPassword123";

        // Act
        String hashedPassword = PasswordUtil.hash(password);

        // Assert
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        assertTrue(hashedPassword.length() > 0);
    }

    @Test
    public void testVerifyPassword() {
        // Arrange
        String password = "testPassword123";
        String hashedPassword = PasswordUtil.hash(password);

        // Act & Assert
        assertTrue(PasswordUtil.matches(password, hashedPassword));
        assertFalse(PasswordUtil.matches("wrongPassword", hashedPassword));
    }

    @Test
    public void testHashConsistency() {
        // Arrange
        String password = "testPassword123";

        // Act
        String hash1 = PasswordUtil.hash(password);
        String hash2 = PasswordUtil.hash(password);

        // Assert
        assertNotEquals(hash1, hash2); // BCrypt generates different hashes each time
        assertTrue(PasswordUtil.matches(password, hash1));
        assertTrue(PasswordUtil.matches(password, hash2));
    }

    @Test
    public void testVerifyWithNullInputs() {
        // Act & Assert
        assertFalse(PasswordUtil.matches(null, "somehash"));
        assertFalse(PasswordUtil.matches("password", null));
        assertFalse(PasswordUtil.matches(null, null));
    }
}
