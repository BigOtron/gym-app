package utility.annotation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import utility.annotation.ValidDateOfBirth;

import java.util.Calendar;
import java.util.Date;

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, Date> {
    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        Date today = new Date();
        if (!value.before(today)) return false;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -150);
        Date limit = cal.getTime();

        return value.after(limit);
    }
}
