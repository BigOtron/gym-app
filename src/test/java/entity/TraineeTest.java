package entity;

import gymapp.entity.Trainee;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Trainee buildValidTrainee() {
        Date dob = Date.from(
                Instant.now().minus(20 * 365, ChronoUnit.DAYS));
        Trainee trainee = new Trainee("Azim",
                "Azimov",
                dob,
                "random street, random region");
        trainee.setUsername("azim.azimov");
        trainee.setPasswordHash("1234567890");
        return trainee;
    }

    @Test
    void allFieldsAreValid() {
        Trainee trainee = buildValidTrainee();
        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertTrue(violations.isEmpty(), "we have some violations: " + violations);
    }

    @Test
    void dobInFuture() {
        Date futureDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        Trainee trainee = buildValidTrainee();
        trainee.setDateOfBirth(futureDate);

        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertFalse(violations.isEmpty());
    }

    @Test
    void dobIsNull() {
        Trainee trainee = buildValidTrainee();
        trainee.setDateOfBirth(null);

        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertFalse(violations.isEmpty());
    }

    @Test
    void addressIsNull() {
        Trainee trainee = buildValidTrainee();
        trainee.setAddress("   ");

        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertFalse(violations.isEmpty());
    }

    @Test
    void addressTooLong() {
        String longAddress = "A".repeat(300);
        Trainee trainee = buildValidTrainee();
        trainee.setAddress(longAddress);

        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertFalse(violations.isEmpty());
    }

    @Test
    void firstNameBlank() {
        Trainee trainee = buildValidTrainee();
        trainee.setFirstName("");

        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertFalse(violations.isEmpty());
    }

    @Test
    void lastNameTooLong() {
        Trainee trainee = buildValidTrainee();
        trainee.setLastName("A".repeat(60));

        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertFalse(violations.isEmpty());
    }

    @Test
    void usernameShort() {
        Trainee trainee = buildValidTrainee();
        trainee.setUsername("abc");

        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertFalse(violations.isEmpty());
    }

    @Test
    void usernameLong() {
        Trainee trainee = buildValidTrainee();
        trainee.setUsername("a".repeat(40));

        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertFalse(violations.isEmpty());
    }

    @Test
    void passwordShort() {
        Trainee trainee = buildValidTrainee();
        trainee.setPasswordHash("12345");

        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertFalse(violations.isEmpty());
    }

    @Test
    void passwordLong() {
        Trainee trainee = buildValidTrainee();
        trainee.setPasswordHash("123456789012345");

        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        assertFalse(violations.isEmpty());
    }

    @Test
    void activeTrueByDefault() {
        Trainee trainee = buildValidTrainee();
        assertTrue(trainee.getIsActive(), "Trainee should be active by default");
    }
}