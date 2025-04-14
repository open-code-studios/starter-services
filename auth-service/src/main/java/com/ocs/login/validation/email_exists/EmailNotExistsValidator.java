package com.ocs.login.validation.email_exists;

import com.ocs.login.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailNotExistsValidator implements ConstraintValidator<EmailNotExists, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && userRepository.existsByEmail(email);
    }
}