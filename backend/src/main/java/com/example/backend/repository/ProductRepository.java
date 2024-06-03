package com.example.backend.repository;

import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE "
            + "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
            + "(:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND "
            + "(:priceFrom IS NULL OR p.price > :priceFrom) AND "
            + "(:priceTo IS NULL OR p.price < :priceTo)")
    List<Product> searchProducts(@Param("name") String name,
                                 @Param("description") String description,
                                 @Param("priceFrom") Double priceFrom,
                                 @Param("priceTo") Double priceTo);



    List<Product> getProductsByUserId(Long id);

    Product getProductsById(Long id);
}
