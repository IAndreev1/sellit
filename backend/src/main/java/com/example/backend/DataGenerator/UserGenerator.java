package com.example.backend.DataGenerator;


import com.example.backend.Entity.ApplicationUser;
import com.example.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Random;


@Component("UserGenerator")
@DependsOn({"CleanDatabase"})
public class UserGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    public void generateApplicationUsers() {
        LOGGER.debug("generating User Entities");

        ApplicationUser testUser = new ApplicationUser();
        testUser.setFirstName("User");
        testUser.setLastName("1");
        testUser.setEmail("user@email.com");
        testUser.setPassword(passwordEncoder.encode("12345678"));

        LOGGER.debug("saving test user: {}", testUser);
        userRepository.save(testUser);


        String[] firstNames = {"John", "Jane", "Alice", "Bob", "Charlie", "David", "Eva", "Frank", "Grace", "Hank", "Ivy"};
        String[] lastNames = {"Doe", "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};
        String[] emails = {"john.doe@example.com", "jane.smith@example.com", "alice.johnson@example.com", "bob.williams@example.com", "charlie.jones@example.com",
                "david.brown@example.com", "eva.davis@example.com", "frank.miller@example.com", "grace.wilson@example.com", "hank.moore@example.com", "ivy.taylor@example.com"};

        for (int i = 0; i < firstNames.length; i++) {
            ApplicationUser user = new ApplicationUser();
            user.setFirstName(firstNames[i]);
            user.setLastName(lastNames[i]);
            user.setEmail(emails[i]);
            user.setPassword(passwordEncoder.encode("12341234"));

            LOGGER.debug("saving user: {}", user);
            userRepository.save(user);
        }
    }
}
