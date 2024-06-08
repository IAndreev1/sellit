package com.example.backend.DataGenerator;

import com.example.backend.Entity.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Profile({"generateData", "test"})
@Component("ProductGenerator")
@DependsOn({"CleanDatabase","UserGenerator"})
public class ProductGenerator {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public ProductGenerator(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void generateProducts() {
        LOGGER.debug("Generating Product Entities");

        List<Product> products = createSampleProducts();
        productRepository.saveAll(products);

        LOGGER.debug("Products generated: {}", products.size());
    }

    private List<Product> createSampleProducts() {
        List<Product> products = new ArrayList<>();

        // Generate sample products
        for (int i = 1; i <= 10; i++) {
            Product product = new Product();
            product.setName("Product " + i);
            product.setDescription("Description of Product " + i);
            product.setPrice(10.0 * i);
            product.setUser(userRepository.getReferenceById((long)i));

            products.add(product);
        }

        return products;
    }

}
