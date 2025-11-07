package entity;

import gymapp.entity.TrainingType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainingTypeTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private TrainingType buildValidTrainingType() {
        TrainingType trainingType = new TrainingType();
        trainingType.setSpecialization("Running");
        return trainingType;
    }

    @Test
    void allValid() {
        TrainingType trainingType = buildValidTrainingType();
        Set<ConstraintViolation<TrainingType>> violations = validator.validate(trainingType);
        assertTrue(violations.isEmpty(), "no violations expected");
    }

    @Test
    void specIsNull() {
        TrainingType trainingType = buildValidTrainingType();
        trainingType.setSpecialization(null);
        Set<ConstraintViolation<TrainingType>> violations = validator.validate(trainingType);
        assertFalse(violations.isEmpty(), "expected violation");
    }

    @Test
    void blankSpec() {
        TrainingType trainingType = buildValidTrainingType();
        trainingType.setSpecialization("   ");
        Set<ConstraintViolation<TrainingType>> violations = validator.validate(trainingType);
        assertFalse(violations.isEmpty(), "expected violation when specialization is blank");
    }

    @Test
    void tooLongSpec() {
        TrainingType trainingType = buildValidTrainingType();
        trainingType.setSpecialization("A".repeat(101));
        Set<ConstraintViolation<TrainingType>> violations = validator.validate(trainingType);
        assertFalse(violations.isEmpty(), "Expected violation when specialization exceeds max length");
    }
}