package org.example.testassignment.ownValidAnnotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ValidBirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {
    @Override
    public void initialize(ValidBirthDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        return birthDate != null && birthDate.isBefore(LocalDate.now());
    }
}
