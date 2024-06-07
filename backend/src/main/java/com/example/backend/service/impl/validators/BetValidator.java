package com.example.backend.service.impl.validators;

import com.example.backend.Endpoints.dto.BetDto;
import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Entity.Bet;
import com.example.backend.Exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Set;

public class BetValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Validator validator;

    public BetValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateForCreate(BetDto betDto) throws ValidationException {
        Set<ConstraintViolation<BetDto>> validationViolations = validator.validate(betDto);
        if (!validationViolations.isEmpty()) {
            throw new ValidationException("The bet is not valid", validationViolations.stream().map(ConstraintViolation::getMessage).toList());
        }
    }
}
