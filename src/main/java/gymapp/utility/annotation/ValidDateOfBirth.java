package gymapp.utility.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import gymapp.utility.annotation.impl.DateOfBirthValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DateOfBirthValidator.class)
@Target({ ElementType.FIELD , ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateOfBirth {
    String message() default "Date of birth must be in the range of 18 to 120 years";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
