package com.example.backend.UnitTests;

import com.example.backend.Endpoints.Mappers.ProductMapper;
import com.example.backend.Endpoints.Mappers.UserMapper;
import com.example.backend.Endpoints.dto.BetDto;
import com.example.backend.Endpoints.dto.BetDtoBuilder;
import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Bet;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.Exceptions.ConflictException;
import com.example.backend.Exceptions.ValidationException;
import com.example.backend.baseTest.TestDataGenerator;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.AuthService;
import com.example.backend.service.BetService;
import com.example.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.IntStream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class BetTests {

    @Autowired
    TestDataGenerator testDataGenerator;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AuthService authService;
    private ApplicationUser applicationUser;
    private UserDetailDto userDetailDto;
    @Autowired
    ProductService productService;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    BetService betService;

    @BeforeEach
    public void cleanUp() throws ValidationException, ConflictException {
        testDataGenerator.cleanUp();
        applicationUser = userRepository.findById(1L).orElseThrow();
        userDetailDto = userMapper.entityToUserDetailDto(applicationUser);
        when(authService.getUserFromToken()).thenReturn(applicationUser);
    }

    @Test
    @DisplayName("Adding a bet for a product should be successful")
    void addBetForProduct_shouldAddTheBetSuccessfully() throws ValidationException {

        ProductDto productDto = productMapper.entityToProductDto(productService.getById(5L));


        BetDto betDto = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();

        Bet createdBet = betService.create(betDto);


        assertAll("Bet",
                () -> assertNotNull(createdBet, "Created bet should not be null"),
                () -> assertNotNull(createdBet.getId(), "Bet ID should not be null after creation"),
                () -> assertEquals(betDto.description(), createdBet.getDescription(), "Bet description should match"),
                () -> assertEquals(betDto.amount(), createdBet.getAmount(), "Bet amount should match"),
                () -> assertEquals(betDto.user().id(), createdBet.getUser().getId(), "Bet user should match"),
                () -> assertEquals(betDto.product().id(), createdBet.getProduct().getId(), "Bet product should match"),
                () -> assertFalse(createdBet.isAccepted(), "Bet should not be accepted by default"),
                () -> assertFalse(createdBet.isRejected(), "Bet should not be rejected by default"),
                () -> assertNotNull(createdBet.getDate(), "Bet date should not be null after creation")
        );
    }

    @Test
    @DisplayName("Adding a bet for a product should fail with invalid data")
    void addBetForProduct_shouldFailWithInvalidData() {
        ProductDto productDto = productMapper.entityToProductDto(productService.getById(5L));

        BetDto invalidBetDto = BetDtoBuilder.builder()
                .amount(-100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();

        assertThrows(ValidationException.class, () -> betService.create(invalidBetDto));
    }

    @Test
    @DisplayName("Updating a bet should be successful")
    void updateBet_shouldUpdateTheBetSuccessfully() throws ValidationException, AuthorizationException {

        ProductDto productDto = productMapper.entityToProductDto(productService.getById(5L));
        BetDto betDto = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();
        Bet createdBet = betService.create(betDto);


        BetDto forUpdateDto = BetDtoBuilder.builder()
                .id(createdBet.getId())
                .amount(150.0)
                .description("My new updated Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();


        Bet updatedBet = betService.update(forUpdateDto);


        assertAll("Updated Bet",
                () -> assertNotNull(updatedBet, "Updated bet should not be null"),
                () -> assertNotNull(updatedBet.getId(), "Bet ID should not be null after update"),
                () -> assertEquals(forUpdateDto.description(), updatedBet.getDescription(), "Bet description should match"),
                () -> assertEquals(forUpdateDto.amount(), updatedBet.getAmount(), "Bet amount should match"),
                () -> assertEquals(forUpdateDto.user().id(), updatedBet.getUser().getId(), "Bet user should match"),
                () -> assertEquals(forUpdateDto.product().id(), updatedBet.getProduct().getId(), "Bet product should match")
        );
    }

    @Test
    @DisplayName("Deleting a bet should be successful")
    void deleteBet_shouldDeleteTheBetSuccessfully() throws ValidationException, AuthorizationException {

        ProductDto productDto = productMapper.entityToProductDto(productService.getById(5L));
        BetDto betDto = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();
        Bet createdBet = betService.create(betDto);

        betService.delete(createdBet.getId());


        Bet deletedBet = betService.getById(createdBet.getId());


        assertNull(deletedBet, "Deleted bet should be null");
    }

    @Test
    @DisplayName("Deleting a bet should fail for unauthorized user")
    void deleteBet_shouldFailForUnauthorizedUser() throws ValidationException {

        ProductDto productDto = productMapper.entityToProductDto(productService.getById(5L));
        BetDto betDto = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();
        Bet createdBet = betService.create(betDto);

        applicationUser = userRepository.findApplicationUserById(2L);
        when(authService.getUserFromToken()).thenReturn(applicationUser);

        assertThrows(AuthorizationException.class, () -> betService.delete(createdBet.getId()));
    }

    @Test
    @DisplayName("Accepting a bet should accept the bet and reject all other bets for the same product")
    void acceptBet_shouldAcceptBet_AndRejectAllOtherBEtsForTheSameProduct() throws ValidationException, AuthorizationException {
        ProductDto productDto = productMapper.entityToProductDto(productService.getById(6L));
        BetDto betDto1 = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();
        BetDto betDto2 = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();
        BetDto betDto3 = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();
        Bet createdBet1 = betService.create(betDto1);
        Bet createdBet2 = betService.create(betDto2);
        Bet createdBet3 = betService.create(betDto3);

        BetDto forUpdateDto = BetDtoBuilder.builder()
                .id(createdBet1.getId())
                .accepted(true)
                .amount(150.0)
                .description("My new updated Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();

        betService.update(forUpdateDto);

        Bet acceptedBet = betService.getById(createdBet1.getId());
        Bet rejectedBet1 = betService.getById(createdBet2.getId());
        Bet rejectedBet2 = betService.getById(createdBet3.getId());

        assertAll(
                () -> assertTrue(acceptedBet.isAccepted()),
                () -> assertFalse(acceptedBet.isRejected()),
                () -> assertTrue(rejectedBet1.isRejected()),
                () -> assertFalse(rejectedBet1.isAccepted()),
                () -> assertTrue(rejectedBet2.isRejected()),
                () -> assertFalse(rejectedBet2.isAccepted())
        );
    }

    @Test
    @DisplayName("Retrieving all bets for the current user should return the corresponding BetDto objects")
    void getAllBetsForUser_shouldReturnBetDtos() throws ValidationException {
        ProductDto productDto = productMapper.entityToProductDto(productService.getById(5L));
        BetDto betDto1 = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();
        BetDto betDto2 = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();
        BetDto betDto3 = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet")
                .user(userDetailDto)
                .product(productDto)
                .build();
        Bet createdBet1 = betService.create(betDto1);
        Bet createdBet2 = betService.create(betDto2);
        Bet createdBet3 = betService.create(betDto3);


        List<BetDto> allBetsForUser = betService.getAllBetsForUser();

        assertNotNull(allBetsForUser, "Returned list should not be null");

        assertAll("Returned BetDto objects",
                () -> assertTrue(allBetsForUser.stream().anyMatch(betDto ->
                        betDto.id().equals(createdBet1.getId()) &&
                                betDto.amount().equals(betDto1.amount()) &&
                                betDto.description().equals(betDto1.description()) &&
                                betDto.product().id().equals(productDto.id())), "BetDto 1 should be returned"),

                () -> assertTrue(allBetsForUser.stream().anyMatch(betDto ->
                        betDto.id().equals(createdBet2.getId()) &&
                                betDto.amount().equals(betDto2.amount()) &&
                                betDto.description().equals(betDto2.description()) &&
                                betDto.product().id().equals(productDto.id())), "BetDto 2 should be returned"),

                () -> assertTrue(allBetsForUser.stream().anyMatch(betDto ->
                        betDto.id().equals(createdBet3.getId()) &&
                                betDto.amount().equals(betDto3.amount()) &&
                                betDto.description().equals(betDto3.description()) &&
                                betDto.product().id().equals(productDto.id())), "BetDto 3 should be returned")
        );
    }
    @Test
    @DisplayName("Retrieving all bets for a product should return the corresponding BetDto objects")
    void getAllBetsForProduct_shouldReturnBetDtos() throws ValidationException {

        ProductDto productDto = productMapper.entityToProductDto(productService.getById(5L));


        BetDto betDto1 = BetDtoBuilder.builder()
                .amount(100.0)
                .description("My new Bet 1")
                .user(userDetailDto)
                .product(productDto)
                .build();
        BetDto betDto2 = BetDtoBuilder.builder()
                .amount(200.0)
                .description("My new Bet 2")
                .user(userDetailDto)
                .product(productDto)
                .build();
        BetDto betDto3 = BetDtoBuilder.builder()
                .amount(300.0)
                .description("My new Bet 3")
                .user(userDetailDto)
                .product(productDto)
                .build();
        betService.create(betDto1);
        betService.create(betDto2);
        betService.create(betDto3);


        List<BetDto> allBetsForProduct = betService.getAllBetsForProduct(productDto.id());


        assertAll("Returned list and sorting",
                () -> assertNotNull(allBetsForProduct, "Returned list should not be null"),
                () -> assertEquals(3, allBetsForProduct.size(), "Returned list should contain 3 BetDto objects"),
                () -> assertTrue(IntStream.range(0, allBetsForProduct.size() - 1)
                                .allMatch(i -> allBetsForProduct.get(i).amount() >= allBetsForProduct.get(i + 1).amount()),
                        "Bets should be sorted by amount in descending order")
        );

    }


}
