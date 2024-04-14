package com.softuni.quotependium.validations.yearIsPastOrPresent;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PastOrPresentYearValidator implements ConstraintValidator<PastOrPresentYear, Integer> {

    @Override
    public void initialize(PastOrPresentYear constraint) {
    }

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return false; // Consider null as invalid
        }

        int currentYear = LocalDate.now().getYear();
        return year <= currentYear;
    }
}