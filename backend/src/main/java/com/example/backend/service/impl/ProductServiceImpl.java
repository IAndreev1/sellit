package com.example.backend.service.impl;

import com.example.backend.Endpoints.Mappers.ProductMapper;
import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Endpoints.dto.ProductSearchDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Product;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.Exceptions.NotFoundException;
import com.example.backend.repository.ProductRepository;
import com.example.backend.security.AuthService;
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

    private final AuthService authService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, AuthService authService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.authService = authService;
    }

    @Override
    public Product create(ProductDto productDto) {
        LOGGER.trace("create({})", productDto);
        ApplicationUser user = authService.getUserFromToken();


        Product product = productMapper.dtoToEntity(productDto);
        product.setUser(user);

        return productRepository.save(product);
    }

    @Override
    public Product update(ProductDto productDto) {
        return null;
    }

    @Override
    public void delete(Long id) throws AuthorizationException {
        Product toDel = productRepository.getProductsById(id);

        if (toDel != null) {
            ApplicationUser user = authService.getUserFromToken();
            if (user.getId().equals(toDel.getUser().getId())) {
                productRepository.delete(toDel);
            } else {
                throw new AuthorizationException("User does not have access to delete this product", new ArrayList<>());
            }
        } else {
            throw new NotFoundException("Product not found with id: " + id);
        }
    }

    @Override
    public List<ProductDto> searchProducts(ProductSearchDto searchParam) {


        return productRepository.searchProducts(searchParam.name(), searchParam.description(), searchParam.priceFrom(), searchParam.priceTo())
                .stream()
                .map(productMapper::entityToProductDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<ProductDto> getUserProducts() {

        ApplicationUser user = authService.getUserFromToken();
        return productRepository.getProductsByUserId(user.getId())
                .stream()
                .map(productMapper::entityToProductDto)
                .collect(Collectors.toCollection(ArrayList::new));

    }


}
