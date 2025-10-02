package entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainingTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Training buildValidTraining() {
        Trainee trainee = new Trainee("Azim",
                "azimov",
                new Date(),
                "123 Street");
        trainee.setUsername("azim.azimov");
        trainee.setPasswordHash("1234567890");

        TrainingType specialization = new TrainingType();
        specialization.setSpecialization("Running");
        Trainer trainer = new Trainer("Azam",
                "Azamov",
                specialization);
        trainer.setPasswordHash("1234567890");
        trainer.setUsername("azam.azamov");

        TrainingType trainingType = new TrainingType();
        trainingType.setSpecialization("Running");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);

        return new Training(null,
                trainee,
                trainer,
                "Morning Session",
                trainingType,
                cal.getTime(),
                45);
    }

    @Test
    void allValid() {
        Training training = buildValidTraining();
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertTrue(violations.isEmpty());
    }

    @Test
    void trainingNameBlank() {
        Training training = buildValidTraining();
        training.setTrainingName(" ");
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertFalse(violations.isEmpty());
    }

    @Test
    void traniningNameLong() {
        Training training = buildValidTraining();
        training.setTrainingName("A".repeat(101));
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertFalse(violations.isEmpty());
    }

    @Test
    void trainingDateNull() {
        Training training = buildValidTraining();
        training.setTrainingDate(null);
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertFalse(violations.isEmpty());
    }

    @Test
    void dateInPast() {
        Training training = buildValidTraining();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        training.setTrainingDate(cal.getTime());
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertFalse(violations.isEmpty());
    }

    @Test
    void durationIsNull() {
        Training training = buildValidTraining();
        training.setDuration(null);
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertFalse(violations.isEmpty());
    }

    @Test
    void durationNegative() {
        Training training = buildValidTraining();
        training.setDuration(-10);
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertFalse(violations.isEmpty());
    }

    @Test
    void longDuration() {
        Training training = buildValidTraining();
        training.setDuration(90);
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertFalse(violations.isEmpty());
    }
}