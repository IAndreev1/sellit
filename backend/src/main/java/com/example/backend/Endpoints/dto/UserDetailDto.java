package com.example.backend.Endpoints.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@RecordBuilder
public record UserDetailDto(
        Long id,

        String firstName,

        String lastName,

        String email,

        String password

) {

}
