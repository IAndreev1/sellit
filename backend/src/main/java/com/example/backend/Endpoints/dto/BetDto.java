package com.example.backend.Endpoints.dto;

import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Product;
import io.soabase.recordbuilder.core.RecordBuilder;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
@RecordBuilder
public record BetDto(
       Long id,
       String description,
       Double amount,
       LocalDate date,
       boolean accepted,
       boolean rejected,
       UserDetailDto user,
       ProductDto product


) {

}
