package com.example.backend.Endpoints.dto;

import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Product;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

public record BetDto(
       Long id,
       String description,
       Double amount,
       LocalDate date,
       UserDetailDto user,
       ProductDto product


) {

}
