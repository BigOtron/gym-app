package entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private User buildValidUser() {
        User user = new User("azim", "azimov");
        user.setPasswordHash("1234567890");
        user.setUsername("azim.azimov");
        return user;
    }

    @Test
    void allValid() {
        User user = buildValidUser();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "violation didn't expect");
    }

    @Test
    void blankFirstName() {
        User user = buildValidUser();
        user.setFirstName("  ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "violation expected");
    }

    @Test
    void longFirstName() {
        User user = buildValidUser();
        user.setFirstName("A".repeat(51));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "violation expected");
    }

    @Test
    void lastNameBlank() {
        User user = buildValidUser();
        user.setLastName("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "violation expected");
    }

    @Test
    void shortUsername() {
        User user = buildValidUser();
        user.setUsername("abc");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "violation expected");
    }

    @Test
    void longUsername() {
        User user = buildValidUser();
        user.setUsername("a".repeat(31));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "violation expected");
    }

    @Test
    void passwordBlank() {
        User user = buildValidUser();
        user.setPasswordHash("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "violation expected");
    }

    @Test
    void shortPassowrd() {
        User user = buildValidUser();
        user.setPasswordHash("12345");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "violation expected");
    }

    @Test
    void longPassword() {
        User user = buildValidUser();
        user.setPasswordHash("12345678901");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "violation expected");
    }

    @Test
    void equalsAndHashCode_differentUsernames() {
        User user1 = new User("Azam", "Azamov");
        user1.setUsername("azam.azamov");
        user1.setPasswordHash("1234567890");
        User user2 = new User("Azim", "Azimov");
        user2.setPasswordHash("abcdefghij");
        user2.setUsername("azim.azimov");

        assertNotEquals(user1, user2, "Users with different usernames should not be equal");
    }
}