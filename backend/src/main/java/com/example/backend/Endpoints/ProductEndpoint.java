package com.example.backend.Endpoints;

import com.example.backend.Endpoints.Mappers.ProductMapper;
import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Entity.Product;
import com.example.backend.service.ProductService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/product")
public class ProductEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProductMapper productMapper;

    private final ProductService service;

    public ProductEndpoint(ProductMapper productMapper, ProductService service) {
        this.productMapper = productMapper;
        this.service = service;
    }


    @PermitAll
    @PostMapping
    ProductDto create(@RequestBody ProductDto productDto){
        LOGGER.trace("create({})", productDto);
        Product createdProduct = service.create(productDto);
        return productMapper.entityToProductDto(createdProduct);
    }
}
