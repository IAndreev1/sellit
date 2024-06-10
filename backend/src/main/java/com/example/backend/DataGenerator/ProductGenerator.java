package com.example.backend.DataGenerator;

import com.example.backend.Entity.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Random;

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

        generateProduct("Laptop", "Experience unparalleled performance and mobility with our cutting-edge laptop. Whether you're working on-the-go or indulging in immersive entertainment, this laptop delivers unparalleled power and versatility.", 1200.0, 1L, "laptop1.jpg");
        generateProduct("Smartphone", "Maximize your productivity and stay connected effortlessly with our feature-rich smartphone. Seamlessly switch between tasks, capture stunning photos, and stay organized on-the-go with this essential companion.", 800.0, 2L, "smartphone1.jpg");
        generateProduct("Headphones", "Dive into an immersive audio experience with our premium headphones. From crisp highs to deep lows, these headphones deliver exceptional sound quality, allowing you to lose yourself in your favorite music and podcasts.", 150.0, 3L, "headphones1.jpg");
        generateProduct("Tablet", "Unleash your creativity and productivity with our versatile tablet. Whether you're sketching ideas, binge-watching your favorite shows, or managing your schedule, this tablet is your ultimate companion.", 500.0, 4L, "tablet1.jpg");
        generateProduct("Smartwatch", "Elevate your fitness journey and stay connected in style with our sleek smartwatch. Track your workouts, receive real-time notifications, and express your personal style with customizable watch faces.", 300.0, 1L, "smartwatch1.jpg");

        generateProduct("Laptop", "Redefine the way you work and play with our advanced laptop. Experience lightning-fast performance, stunning visuals, and seamless multitasking, making every task effortless and enjoyable.", 1200.0, 6L, "laptop2.jpg");
        generateProduct("Smartphone", "Stay ahead of the curve with our next-generation smartphone. With innovative features and cutting-edge technology, this smartphone empowers you to do more, stay organized, and capture life's moments with clarity.", 800.0, 7L, "smartphone2.jpg");
        generateProduct("Headphones", "Immerse yourself in pure sound perfection with our premium headphones. Designed for audiophiles, these headphones deliver crystal-clear audio and all-day comfort, ensuring an unparalleled listening experience.", 150.0, 9L, "headphones2.jpg");
        generateProduct("Tablet", "Unleash your creativity and enhance your productivity with our intuitive tablet. Whether you're sketching ideas, binge-watching your favorite shows, or tackling work tasks, this tablet adapts to your needs.", 500.0, 9L, "tablet2.jpg");
        generateProduct("Smartwatch", "Stay connected, motivated, and stylish with our versatile smartwatch. From tracking your fitness goals to managing your day-to-day activities, this smartwatch keeps you in control while complementing your personal style.", 300.0, 5L, "smartwatch2.jpg");

        generateProduct("Laptop", "Empower your creativity and productivity with our premium laptop. Engineered for performance and reliability, this laptop transforms the way you work, enabling you to accomplish more with ease.", 1200.0, 1L, "laptop3.jpg");
        generateProduct("Smartphone", "Experience innovation at your fingertips with our cutting-edge smartphone. Packed with advanced features and intuitive capabilities, this smartphone enhances every aspect of your digital life.", 800.0, 2L, "smartphone3.jpg");
        generateProduct("Headphones", "Immerse yourself in the ultimate listening experience with our elite headphones. Crafted for discerning audiophiles, these headphones deliver unparalleled sound quality and comfort for extended listening sessions.", 150.0, 3L, "headphones3.jpg");
        generateProduct("Tablet", "Unleash your imagination and productivity with our versatile tablet. Whether you're creating, consuming, or collaborating, this tablet empowers you to do more and express your creativity anywhere.", 500.0, 7L, "tablet3.jpg");
        generateProduct("Smartwatch", "Achieve your fitness goals and stay connected in style with our sleek smartwatch. From tracking your workouts to managing notifications, this smartwatch seamlessly integrates into your active lifestyle.", 300.0, 8L, "smartwatch3.jpg");



        LOGGER.debug("Products generated.");
    }




    private void generateProduct(String name, String description, double price, Long userId, String photoName) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setUser(userRepository.getReferenceById(userId)); // Assuming user ID is provided

        // Set photo for the product
        try {
            byte[] photoBytes = loadPhoto(photoName);
            product.setImageData(photoBytes);
        } catch (IOException e) {
            LOGGER.error("Failed to load photo for product '{}'.", name, e);
        }

        productRepository.save(product);
        LOGGER.debug("Product '{}' generated.", name);
    }


    private byte[] loadPhoto(String photoName) throws IOException {
        String photoPath = "static/photos/" + photoName;
        Resource resource = new ClassPathResource(photoPath);
        InputStream inputStream = resource.getInputStream();

        // Convert InputStream to byte array
        byte[] data = StreamUtils.copyToByteArray(inputStream);

        // Close the stream
        inputStream.close();

        return data;
    }



}
