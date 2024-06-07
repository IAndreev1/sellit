package com.example.backend.service.impl;

import com.example.backend.Endpoints.Mappers.BetMapper;
import com.example.backend.Endpoints.Mappers.ProductMapper;
import com.example.backend.Endpoints.Mappers.UserMapper;
import com.example.backend.Endpoints.dto.BetDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Bet;
import com.example.backend.Entity.Product;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.Exceptions.NotFoundException;
import com.example.backend.Exceptions.ValidationException;
import com.example.backend.repository.BetRepository;
import com.example.backend.security.AuthService;
import com.example.backend.service.BetService;
import com.example.backend.service.impl.validators.BetValidator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BetServiceImpl implements BetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final BetRepository betRepository;

    private final BetMapper betMapper;

    private final AuthService authService;

    private final UserMapper userMapper;

    private final ProductMapper productMapper;

    private final BetValidator betValidator;

    public BetServiceImpl(BetRepository betRepository, BetMapper betMapper, AuthService authService, UserMapper userMapper, ProductMapper productMapper, BetValidator betValidator) {
        this.betRepository = betRepository;
        this.betMapper = betMapper;
        this.authService = authService;
        this.userMapper = userMapper;
        this.productMapper = productMapper;
        this.betValidator = betValidator;
    }

    @Override
    @Transactional
    public Bet create(BetDto betDto) throws ValidationException {
        betValidator.validateForCreate(betDto);
        LOGGER.trace("create({})", betDto);
        ApplicationUser user = authService.getUserFromToken();
        Bet toCreate = betMapper.dtoToEntity(betDto);
        toCreate.setUser(user);
        toCreate.setDate(LocalDate.now());
        return betRepository.save(toCreate);
    }

    @Override
    @Transactional
    public Bet update(BetDto betDto) throws AuthorizationException {
        Bet existingBet = betRepository.getBetById(betDto.id());
        if (existingBet != null) {
            ApplicationUser user = authService.getUserFromToken();

            existingBet.setDescription(betDto.description());
            existingBet.setAmount(betDto.amount());
            existingBet.setRejected(betDto.rejected());
            if (betDto.accepted() && !existingBet.isAccepted()) {
                existingBet.setAccepted(true);
                acceptBet(existingBet);
            }
            existingBet.setAccepted(betDto.accepted());
            return betRepository.save(existingBet);

        } else {
            throw new NotFoundException("Bet with id: " + betDto.id() + " not found");
        }

    }

    @Override
    @Transactional
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

    @Override
    public List<BetDto> getAllBetsForUser() {
        ApplicationUser user = authService.getUserFromToken();
        List<Bet> bets = betRepository.getBetsByUser(user);
        return bets.stream()
                .map(betMapper::entityToBetDto)
                .collect(Collectors.toCollection(ArrayList::new));

    }


    private void acceptBet(Bet bet) throws AuthorizationException {
        Product product = bet.getProduct();
        List<Bet> betsForTheSameProduct = betRepository.getBetsByProductId(product.getId());

        for (Bet b : betsForTheSameProduct) {
            if (!Objects.equals(b.getId(), bet.getId())) {
                update(new BetDto(b.getId(), b.getDescription(), b.getAmount(), b.getDate(), false, true, userMapper.entityToUserDetailDto(b.getUser()), productMapper.entityToProductDto(product)));
            }
        }
    }


}
