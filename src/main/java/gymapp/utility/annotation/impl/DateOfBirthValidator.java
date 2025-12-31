package gymapp.utility.annotation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import gymapp.utility.annotation.ValidDateOfBirth;

import java.util.Calendar;
import java.util.Date;

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, Date> {
    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -120);
        Date oldestAllowedYear = cal.getTime();

        cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);
        Date youngestAllowedYear = cal.getTime();

        return value.after(oldestAllowedYear) && value.before(youngestAllowedYear);
    }
}
