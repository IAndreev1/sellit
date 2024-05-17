package com.example.backend.service;

import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Entity.Product;

public interface ProductService {

    Product create(ProductDto productDto);

    Product update(ProductDto productDto);

    void delete(Long id);
}
