package com.example.backend.service.impl;

import com.example.backend.Endpoints.Mappers.ProductMapper;
import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Endpoints.dto.ProductSearchDto;
import com.example.backend.Entity.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product create(ProductDto productDto) {
        LOGGER.trace("create({})", productDto);

        Product product = productMapper.dtoToEntity(productDto);

        return productRepository.save(product);
    }

    @Override
    public Product update(ProductDto productDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<ProductDto> searchProducts(ProductSearchDto searchParam) {
        return productRepository.searchProducts(searchParam.name(), searchParam.description(), searchParam.priceFrom(), searchParam.priceTo())
                .stream()
                .map(productMapper::entityToProductDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }



}
