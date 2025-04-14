package com.ocs.login.validation.email_exists;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailNotExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailNotExists {
    String message() default "Email not exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}