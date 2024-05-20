package com.example.backend.service;

import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Endpoints.dto.ProductSearchDto;
import com.example.backend.Entity.Product;

import java.util.List;

public interface ProductService {

    Product create(ProductDto productDto);

    Product update(ProductDto productDto);

    void delete(Long id);

    List<ProductDto> searchProducts(ProductSearchDto productSearchDto);
}
