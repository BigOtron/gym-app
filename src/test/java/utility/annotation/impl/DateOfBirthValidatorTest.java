package utility.annotation.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateOfBirthValidatorTest {

    private DateOfBirthValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DateOfBirthValidator();
    }

    @Test
    void testNullDate() {
        assertTrue(validator.isValid(null, null));
    }

    @Test
    void testFutureDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date futureDate = cal.getTime();

        assertFalse(validator.isValid(futureDate, null));
    }

    @Test
    void testTooOld() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -200);
        Date oldDate = cal.getTime();

        assertFalse(validator.isValid(oldDate, null));
    }

    @Test
    void testValidAge() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -30);
        Date validDate = cal.getTime();

        assertTrue(validator.isValid(validDate, null));
    }

    @Test
    void testExact150() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -150);
        Date boundaryDate = cal.getTime();

        assertFalse(validator.isValid(boundaryDate, null));
    }
}