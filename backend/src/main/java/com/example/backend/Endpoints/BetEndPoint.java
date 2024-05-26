package com.example.backend.Endpoints;

import com.example.backend.Endpoints.Mappers.BetMapper;
import com.example.backend.Endpoints.dto.BetDto;
import com.example.backend.Entity.Bet;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.service.BetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/bet")
public class BetEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final BetService betService;

    private final BetMapper betMapper;

    public BetEndPoint(BetService betService, BetMapper betMapper) {
        this.betService = betService;
        this.betMapper = betMapper;
    }

    @Secured("ROLE_USER")
    @PostMapping
    public BetDto create(@RequestBody BetDto betDto) {
        LOGGER.trace("create({})", betDto);
        Bet createdBet = betService.create(betDto);
        return betMapper.entityToBetDto(createdBet);
    }

    @Secured("ROLE_USER")
    @PutMapping
    public BetDto update(BetDto betDto) throws AuthorizationException {
        return betMapper.entityToBetDto(betService.update(betDto));
    }

    @Secured("ROLE_USER")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
    }

}
