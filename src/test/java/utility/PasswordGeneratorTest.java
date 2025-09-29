package utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordGeneratorTest {

    private final PasswordGenerator generator = new PasswordGenerator();

    @Test
    void testPasswordLength() {
        String password = generator.generatePassword(10);
        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void testPasswordUniqueness() {
        String password1 = generator.generatePassword(10);
        String password2 = generator.generatePassword(10);
        assertNotEquals(password1, password2);
    }

    @Test
    void testPasswordCharacters() {
        String password = generator.generatePassword(20);
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        for (char c : password.toCharArray()) {
            assertTrue(allowedChars.indexOf(c) >= 0, "Invalid character found: " + c);
        }
    }

    @Test
    void testDifferentLengths() {
        String shortPwd = generator.generatePassword(5);
        String longPwd = generator.generatePassword(30);
        assertEquals(5, shortPwd.length());
        assertEquals(30, longPwd.length());
    }
}