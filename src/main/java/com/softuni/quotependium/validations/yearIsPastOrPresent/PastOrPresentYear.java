package com.softuni.quotependium.validations.yearIsPastOrPresent;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PastOrPresentYearValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PastOrPresentYear {

    String message() default "Year must be in the past or present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
