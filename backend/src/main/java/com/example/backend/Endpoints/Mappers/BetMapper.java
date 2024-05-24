package com.example.backend.Endpoints.Mappers;

import com.example.backend.Endpoints.dto.BetDto;
import com.example.backend.Entity.Bet;
import org.mapstruct.Mapper;

@Mapper
public  abstract class BetMapper {

    public abstract BetDto entityToBetDto(Bet bet);

    public abstract Bet dtoToEntity(BetDto betDto);
}
