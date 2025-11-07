package gymapp.utility;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordGeneratorTest {

    @Test
    void testLength() {
        int length = 12;
        String password = PasswordGenerator.generatePassword(length);

        assertNotNull(password);
        assertEquals(length, password.length());
    }

    @Test
    void testValidChars() {
        int length = 20;
        String password = PasswordGenerator.generatePassword(length);

        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

        for (char c : password.toCharArray()) {
            assertTrue(validChars.indexOf(c) >= 0,
                    "Invalid character found in password: " + c);
        }
    }

    @RepeatedTest(5)
    void testRandom() {
        int length = 10;
        String password1 = PasswordGenerator.generatePassword(length);
        String password2 = PasswordGenerator.generatePassword(length);

        assertNotEquals(password1, password2, "Passwords should not be equal in each calls");
    }
}