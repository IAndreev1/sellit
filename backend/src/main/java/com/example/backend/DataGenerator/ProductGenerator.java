package com.example.backend.DataGenerator;

import com.example.backend.Entity.ApplicationUser;
import com.example.backend.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile({"generateData", "test"})
@Component("ProductGenerator")
@DependsOn({"CleanDatabase"})
public class ProductGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProductRepository productRepository;


    public ProductGenerator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @PostConstruct
    public void generateProducts() {
        LOGGER.debug("generating Product Entities");


    }
}
