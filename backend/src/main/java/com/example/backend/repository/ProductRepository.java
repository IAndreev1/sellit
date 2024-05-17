package com.example.backend.repository;

import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
