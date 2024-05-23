package com.example.backend.Endpoints;

import com.example.backend.Endpoints.Mappers.ProductMapper;
import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Endpoints.dto.ProductSearchDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Product;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.security.AuthService;
import com.example.backend.service.ProductService;
import jakarta.annotation.security.PermitAll;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/product")
public class ProductEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProductMapper productMapper;

    private final ProductService service;

    private final AuthService authService;

    public ProductEndpoint(ProductMapper productMapper, ProductService service, AuthService authService) {
        this.productMapper = productMapper;
        this.service = service;
        this.authService = authService;
    }


    @PermitAll
    @PostMapping
    public ProductDto create(@RequestBody ProductDto productDto) {
        LOGGER.trace("create({})", productDto);
        Product createdProduct = service.create(productDto);
        return productMapper.entityToProductDto(createdProduct);
    }


    @PermitAll
    @GetMapping
    public List<ProductDto> searchProducts(ProductSearchDto searchParam) {
        LOGGER.trace("searchProducts({})", searchParam);

        return service.searchProducts(searchParam);
    }

    @Secured("ROLE_USER")
    @PutMapping()
    public ProductDto updateProduct(@RequestBody ProductDto productDto) throws AuthorizationException {
        return productMapper.entityToProductDto(service.update(productDto));
    }

    @Secured("ROLE_USER")
    @GetMapping("/forUser")
    public List<ProductDto> getUserProducts() {
        return service.getUserProducts();
    }

    @Secured("ROLE_USER")
    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable Long id) throws AuthorizationException {
        service.delete(id);
    }

    @Secured("ROLE_USER")
    @GetMapping("myProd/{id}")
    public ProductDto getById(@PathVariable Long id) {
        return productMapper.entityToProductDto(service.getById(id));
    }


}
