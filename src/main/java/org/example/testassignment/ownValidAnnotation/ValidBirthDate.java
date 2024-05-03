package org.example.testassignment.ownValidAnnotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidBirthDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBirthDate {
    String message() default "Birth date must be earlier than the current date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
