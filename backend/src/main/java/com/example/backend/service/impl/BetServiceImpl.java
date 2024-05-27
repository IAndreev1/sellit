package com.example.backend.service.impl;

import com.example.backend.Endpoints.Mappers.BetMapper;
import com.example.backend.Endpoints.Mappers.ProductMapper;
import com.example.backend.Endpoints.Mappers.UserMapper;
import com.example.backend.Endpoints.dto.BetDto;
import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Bet;
import com.example.backend.Entity.Product;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.Exceptions.NotFoundException;
import com.example.backend.repository.BetRepository;
import com.example.backend.security.AuthService;
import com.example.backend.service.BetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BetServiceImpl implements BetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final BetRepository betRepository;

    private final BetMapper betMapper;

    private final AuthService authService;

    private final UserMapper userMapper;

    private final ProductMapper productMapper;

    public BetServiceImpl(BetRepository betRepository, BetMapper betMapper, AuthService authService, UserMapper userMapper, ProductMapper productMapper) {
        this.betRepository = betRepository;
        this.betMapper = betMapper;
        this.authService = authService;
        this.userMapper = userMapper;
        this.productMapper = productMapper;
    }

    @Override
    public Bet create(BetDto betDto) {
        LOGGER.trace("create({})", betDto);
        ApplicationUser user = authService.getUserFromToken();
        Bet toCreate = betMapper.dtoToEntity(betDto);
        toCreate.setUser(user);
        toCreate.setDate(LocalDate.now());
        return betRepository.save(toCreate);
    }

    @Override
    public Bet update(BetDto betDto) throws AuthorizationException {
        Bet existingBet = betRepository.getBetById(betDto.id());
        if (existingBet != null) {
            ApplicationUser user = authService.getUserFromToken();
            if (user.getId().equals(existingBet.getUser().getId())) {
                existingBet.setUser(userMapper.userDetailDtoToEntity(betDto.user()));
                existingBet.setProduct(productMapper.dtoToEntity(betDto.product()));
                existingBet.setDescription(betDto.description());
                existingBet.setAmount(betDto.amount());
                return betRepository.save(existingBet);
            } else {
                throw new AuthorizationException("User does not have access to update this product", new ArrayList<>());
            }
        } else {
            throw new NotFoundException("Bet with id: " + betDto.id() + " not found");
        }
    }

    @Override
    public void delete(Long id) throws AuthorizationException {
        Bet toDel = betRepository.getBetById(id);
        if (toDel != null) {
            ApplicationUser user = authService.getUserFromToken();
            if (user.getId().equals(toDel.getUser().getId())) {
                betRepository.delete(toDel);
            } else {
                throw new AuthorizationException("User does not have access to delete this product", new ArrayList<>());
            }
        } else {
            throw new NotFoundException("Product not found with id: " + id);
        }
    }

    @Override
    public List<BetDto> getAllBetsForProduct(Long prodId) {
        return betRepository.getBetsByProductId(prodId).stream()
                .sorted(Comparator.comparingDouble(Bet::getAmount).reversed()) // Sort by amount in descending order
                .map(betMapper::entityToBetDto)
                .collect(Collectors.toList());
    }

    public List<BetDto> getAllBetsForUser() {
        ApplicationUser user = authService.getUserFromToken();
        List<Bet> bets = betRepository.getBetsByUser(user);
        return null;
    }


}
