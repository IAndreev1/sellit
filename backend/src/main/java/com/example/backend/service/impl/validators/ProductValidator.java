package com.example.backend.service.impl.validators;

import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.lang.invoke.MethodHandles;
import java.util.Set;
@Component
public class ProductValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Validator validator;

    public ProductValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateForCreate(ProductDto productDto) throws ValidationException {
        Set<ConstraintViolation<ProductDto>> validationViolations = validator.validate(productDto);
        if (!validationViolations.isEmpty()) {
            throw new ValidationException("The product is not valid", validationViolations.stream().map(ConstraintViolation::getMessage).toList());
        }
    }
}
