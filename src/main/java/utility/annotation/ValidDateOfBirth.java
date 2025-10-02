package utility.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import utility.annotation.impl.DateOfBirthValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateOfBirthValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateOfBirth {
    String message() default "Date of birth must be in the past and not more than 150 years ago";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
