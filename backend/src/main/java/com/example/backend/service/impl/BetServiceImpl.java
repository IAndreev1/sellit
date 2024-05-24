package com.example.backend.service.impl;

import com.example.backend.Endpoints.Mappers.BetMapper;
import com.example.backend.Endpoints.dto.BetDto;
import com.example.backend.Entity.Bet;
import com.example.backend.repository.BetRepository;
import com.example.backend.service.BetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class BetServiceImpl implements BetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final BetRepository betRepository;

    private final BetMapper betMapper;

    public BetServiceImpl(BetRepository betRepository, BetMapper betMapper) {
        this.betRepository = betRepository;
        this.betMapper = betMapper;
    }

    @Override
    public Bet create(BetDto betDto) {
        LOGGER.trace("create({})", betDto);
        return betRepository.save(betMapper.dtoToEntity(betDto));
    }

    @Override
    public Bet update(BetDto betDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
