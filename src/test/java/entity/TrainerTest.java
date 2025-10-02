package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Trainer buildValidTrainer() {
        TrainingType specialization = new TrainingType();
        specialization.setSpecialization("Running");
        Trainer trainer = new Trainer("Azim", "Azimov", specialization);
        trainer.setUsername("azim.azimov");
        trainer.setPasswordHash("1234567890");
        return trainer;
    }

    @Test
    void allValid() {
        Trainer trainer = buildValidTrainer();
        Set<ConstraintViolation<Trainer>> violations = validator.validate(trainer);
        assertTrue(violations.isEmpty());
    }

    @Test
    void firstNameBlank() {
        Trainer trainer = buildValidTrainer();
        trainer.setFirstName(" ");
        Set<ConstraintViolation<Trainer>> violations = validator.validate(trainer);
        assertFalse(violations.isEmpty());
    }

    @Test
    void lastNameBlank() {
        Trainer trainer = buildValidTrainer();
        trainer.setLastName("");
        Set<ConstraintViolation<Trainer>> violations = validator.validate(trainer);
        assertFalse(violations.isEmpty());
    }

    @Test
    void usernameShort() {
        Trainer trainer = buildValidTrainer();
        trainer.setUsername("ab");
        Set<ConstraintViolation<Trainer>> violations = validator.validate(trainer);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shortPassword() {
        Trainer trainer = buildValidTrainer();
        trainer.setPasswordHash("short");
        Set<ConstraintViolation<Trainer>> violations = validator.validate(trainer);
        assertFalse(violations.isEmpty());
    }

    @Test
    void specIsNull() {
        Trainer trainer = buildValidTrainer();
        trainer.setSpecialization(null);
        Set<ConstraintViolation<Trainer>> violations = validator.validate(trainer);
        assertFalse(violations.isEmpty());
    }
}