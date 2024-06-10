package com.example.backend.UnitTests;

import com.example.backend.Endpoints.Mappers.ProductMapper;
import com.example.backend.Endpoints.Mappers.UserMapper;
import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Endpoints.dto.ProductDtoBuilder;
import com.example.backend.Endpoints.dto.ProductSearchDto;
import com.example.backend.Endpoints.dto.ProductSearchDtoBuilder;
import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Product;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.Exceptions.ConflictException;
import com.example.backend.Exceptions.ValidationException;
import com.example.backend.baseTest.TestDataGenerator;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.AuthService;
import com.example.backend.service.ProductService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ProductTests {

    @Autowired
    TestDataGenerator testDataGenerator;

    @Autowired
    ProductService productService;

    private ApplicationUser applicationUser;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AuthService authService;
    @Autowired
    private UserMapper userMapper;

    private UserDetailDto userDetailDto;
    @Autowired
    private ProductMapper productMapper;

    @BeforeEach
    public void cleanUp() throws ValidationException, ConflictException {
        testDataGenerator.cleanUp();
        applicationUser = userRepository.findById(1L).orElseThrow();
        userDetailDto = userMapper.entityToUserDetailDto(applicationUser);
        when(authService.getUserFromToken()).thenReturn(applicationUser);
    }


    @Test
    @DisplayName("Creating a product should succeed with valid data")
    void createNewProduct_shouldCreteTheProductSuccessfully() throws ValidationException {
        ProductDto productDto = ProductDtoBuilder.builder()
                .name("testProduct")
                .description("productDescription")
                .price(10.0)
                .user(userDetailDto)
                .build();

        Product createdProduct = productService.create(productDto);

        assertAll(

                () -> assertNotNull(createdProduct, "Product should not be null after creation"),
                () -> assertNotNull(createdProduct.getId(), "Product ID should not be null after creation"),
                () -> assertEquals("testProduct", createdProduct.getName(), "Product name should match"),
                () -> assertEquals("productDescription", createdProduct.getDescription(), "Product description should match"),
                () -> assertEquals(10.0, createdProduct.getPrice(), "Product price should match"),
                () -> assertNotNull(createdProduct.getUser(), "Product user should not be null"),
                () -> assertEquals(1L, createdProduct.getUser().getId(), "Product user ID should match")
        );

    }

    @Test
    @DisplayName("Creating a product should fail when product name is null")
    void createProduct_shouldFail_whenNameIsNull() {
        ProductDto invalidProductDto = ProductDtoBuilder.builder()
                .name(null)
                .description("productDescription")
                .price(10.0)
                .user(userDetailDto)
                .build();

        assertThrows(ValidationException.class, () -> {
            productService.create(invalidProductDto);
        });
    }

    @Test
    @DisplayName("Creating a product should fail when product description exceeds 500 characters")
    void createProduct_shouldFail_whenDescriptionIsTooLong() {
        String longDescription = "a".repeat(501);
        ProductDto invalidProductDto = ProductDtoBuilder.builder()
                .name("testProduct")
                .description(longDescription)
                .price(10.0)
                .user(userDetailDto)
                .build();

        assertThrows(ValidationException.class, () -> {
            productService.create(invalidProductDto);
        });
    }

    @Test
    @DisplayName("Creating a product should fail when product price is null")
    void createProduct_shouldFail_whenPriceIsNull() {
        ProductDto invalidProductDto = ProductDtoBuilder.builder()
                .name("testProduct")
                .description("productDescription")
                .price(null)
                .user(userDetailDto)
                .build();

        assertThrows(ValidationException.class, () -> {
            productService.create(invalidProductDto);
        });
    }

    @Test
    @DisplayName("Updating a product should succeed with valid data")
    void updateProduct_shouldUpdateTheProductSuccessfully() throws AuthorizationException, ValidationException {
        ProductDto productDto = ProductDtoBuilder.builder()
                .name("testProduct")
                .description("productDescription")
                .price(10.0)
                .sold(false)
                .user(userDetailDto)
                .build();

        Product createdProduct = productService.create(productDto);

        ProductDto toUpdate = new ProductDto(
                createdProduct.getId(),
                "new Name",
                "new Description",
                createdProduct.getPrice() + 10.0,
                false,
                userDetailDto,
                createdProduct.getImageData()
        );

        Product updatedProduct = productService.update(toUpdate);

        assertAll(
                () -> assertNotNull(updatedProduct, "Updated product should not be null"),
                () -> assertEquals(createdProduct.getId(), updatedProduct.getId(), "Product ID should match"),
                () -> assertEquals("new Name", updatedProduct.getName(), "Product name should match"),
                () -> assertEquals("new Description", updatedProduct.getDescription(), "Product description should match"),
                () -> assertEquals(createdProduct.getPrice() + 10.0, updatedProduct.getPrice(), "Product price should match"),
                () -> assertNotNull(updatedProduct.getUser(), "Product user should not be null"),
                () -> assertEquals(userDetailDto.id(), updatedProduct.getUser().getId(), "Product user ID should match"),
                () -> assertArrayEquals(createdProduct.getImageData(), updatedProduct.getImageData(), "Product image data should match")
        );
    }

    @Test
    @DisplayName("Deleting a product should succeed and remove the product")
    void deleteProduct_shouldDeleteTheProductSuccessfully() throws ValidationException, AuthorizationException {
        ProductDto productDto = ProductDtoBuilder.builder()
                .name("testProduct")
                .description("productDescription")
                .price(10.0)
                .user(userDetailDto)
                .sold(false)
                .build();

        Product createdProduct = productService.create(productDto);

        productService.delete(createdProduct.getId());

        assertNull(productService.getById(createdProduct.getId()));


    }

    @Test
    @DisplayName("Deleting a product should fail if the user is not authorized")
    void deleteProduct_shouldFailForUnauthorizedUser() {

        applicationUser.setId(2L);
        when(authService.getUserFromToken()).thenReturn(applicationUser);

        Long productId = 1L;

        assertThrows(AuthorizationException.class, () -> {
            productService.delete(productId);
        });
    }

    @Test
    @DisplayName("Updating a product should fail if the user is not authorized")
    void updateProduct_shouldFailForUnauthorizedUser() {
        applicationUser.setId(2L);

        when(authService.getUserFromToken()).thenReturn(applicationUser);

        ProductDto productDto = new ProductDto(
                1L,
                "new Name",
                "new Description",
                20.0,
                false,
                userDetailDto,
                null
        );

        assertThrows(AuthorizationException.class, () -> {
            productService.update(productDto);
        });
    }

    @Test
    @DisplayName("Searching for products should return matching ProductDto objects")
    void searchProduct_shouldReturnTheProducts() throws ValidationException {
        ProductDto productDto = ProductDtoBuilder.builder()
                .name("testProduct")
                .description("productDescription")
                .price(10.0)
                .user(userDetailDto)
                .build();

        productService.create(productDto);

        ProductSearchDto searchParam = ProductSearchDtoBuilder.builder()
                .name("testProduct")
                .build();

        List<ProductDto> result = productService.searchProducts(searchParam);

        assertAll("Search Result",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(1, result.size(), "Expected one product in the result list"),
                () -> {
                    ProductDto resultProductDto = result.get(0);
                    assertEquals(searchParam.name(), resultProductDto.name(), "Product name should match search criteria");
                }
        );
    }


    @Test
    @DisplayName("Searching for products with invalid parameters should throw ValidationException")
    void searchProduct_shouldThrowValidationExceptionForInvalidParameters() {
        ProductSearchDto searchParam = ProductSearchDtoBuilder.builder()
                .name("testProduct")
                .priceFrom(-10.0)
                .priceTo(0.0)
                .build();


        assertThrows(ValidationException.class, () -> {
            productService.searchProducts(searchParam);
        });
    }


}
