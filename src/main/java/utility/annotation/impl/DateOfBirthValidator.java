package utility.annotation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import utility.annotation.ValidDateOfBirth;

import java.util.Calendar;
import java.util.Date;

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, Date> {
    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -150);
        Date limit = cal.getTime();

        return value.before(today) && value.after(limit);
    }
}
