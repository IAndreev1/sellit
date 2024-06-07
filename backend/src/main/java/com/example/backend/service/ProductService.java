package com.example.backend.service;

import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Endpoints.dto.ProductSearchDto;
import com.example.backend.Entity.Product;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.Exceptions.ValidationException;

import java.util.List;
/**
 * Service interface for managing product operations.
 */
public interface ProductService {

    /**
     * Creates a new product based on the provided product DTO.
     *
     * @param productDto The product DTO containing information about the product to be created.
     * @return The created product.
     */
    Product create(ProductDto productDto) throws ValidationException;

    /**
     * Updates an existing product based on the provided product DTO.
     *
     * @param productDto The product DTO containing updated information about the product.
     * @return The updated product.
     */
    Product update(ProductDto productDto) throws AuthorizationException;

    /**
     * Deletes a product with the specified ID.
     *
     * @param id The ID of the product to be deleted.
     */
    void delete(Long id) throws AuthorizationException;

    /**
     * Searches for products based on the criteria specified in the product search DTO.
     *
     * @param productSearchDto The product search DTO containing search criteria.
     * @return A list of product DTOs matching the search criteria.
     */
    List<ProductDto> searchProducts(ProductSearchDto productSearchDto);

    /**
     * Retrieves products associated with the current user.
     *
     * @return A list of product DTOs belonging to the current user.
     */
    List<ProductDto> getUserProducts();

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The product with the specified ID.
     */
    Product getById(Long id);

}
