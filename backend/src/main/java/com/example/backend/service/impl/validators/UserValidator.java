package com.example.backend.service.impl.validators;

import com.example.backend.Endpoints.dto.BetDto;
import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Set;

public class UserValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Validator validator;

    public UserValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateForCreate(UserDetailDto userDetailDto) throws ValidationException {
        Set<ConstraintViolation<UserDetailDto>> validationViolations = validator.validate(userDetailDto);
        if (!validationViolations.isEmpty()) {
            throw new ValidationException("The user is not valid", validationViolations.stream().map(ConstraintViolation::getMessage).toList());
        }
    }
}
