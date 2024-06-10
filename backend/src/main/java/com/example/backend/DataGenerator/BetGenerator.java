package com.example.backend.DataGenerator;

import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Bet;
import com.example.backend.Entity.Product;
import com.example.backend.repository.BetRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Profile({"generateData", "test"})
@Component("BetGenerator")
@DependsOn({"CleanDatabase", "UserGenerator", "ProductGenerator"})
public class BetGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final BetRepository betRepository;

    public BetGenerator(ProductRepository productRepository, UserRepository userRepository, BetRepository betRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.betRepository = betRepository;
    }

    @PostConstruct
    public void generateBets() {
        LOGGER.debug("Generating Bet Entities");

        List<Product> products = productRepository.findAll();
        List<ApplicationUser> users = userRepository.findAll();
        Random random = new Random();

        for (Product product : products) {
            int numberOfBets = random.nextInt(4) + 1;

            for (int i = 0; i < numberOfBets; i++) {
                ApplicationUser user = users.get(random.nextInt(users.size()));

                if (!user.getId().equals(product.getUser().getId())) {
                    Bet bet = new Bet();
                    bet.setProduct(product);
                    bet.setUser(user);


                    String betReason = generateBetReason(product);
                    bet.setDescription(betReason);

                    double betAmount = product.getPrice() + random.nextDouble() * product.getPrice();
                    betAmount = Math.round(betAmount * 100.0) / 100.0;
                    bet.setAmount(betAmount);

                    bet.setDate(LocalDate.now().minusDays(random.nextInt(30)));

                    betRepository.save(bet);
                    LOGGER.debug("Bet of amount '{}' for product '{}' generated with reason: '{}'.", bet.getAmount(), product.getName(), bet.getDescription());
                }
            }
        }

        LOGGER.debug("Bets generated.");
    }


    private String generateBetReason(Product product) {
        String[] reasons = {
                "I need this " + product.getName() + " for my upcoming project.",
                "This " + product.getName() + " is perfect for my daily activities.",
                "I've been looking for a " + product.getName() + " like this for a while now.",
                "This " + product.getName() + " will greatly improve my productivity.",
                "I've heard great reviews about this " + product.getName() + " and want to try it out.",
                "My current " + product.getName() + " is outdated, and I need an upgrade.",
                "I believe this " + product.getName() + " will help me achieve my goals.",
                "This " + product.getName() + " has all the features I've been searching for.",
                "I'm impressed by the design and functionality of this " + product.getName() + ".",
                "I've seen others using this " + product.getName() + " and want to experience its benefits.",
                "I'm confident that this " + product.getName() + " will save me time and effort.",
                "Owning this " + product.getName() + " would enhance my professional image.",
                "This " + product.getName() + " aligns perfectly with my interests and hobbies.",
                "I've compared several " + product.getName() + " options, and this one stands out as the best.",
                "I've been planning to purchase a " + product.getName() + " for a while, and this seems like the right choice."
        };

        Random random = new Random();
        return reasons[random.nextInt(reasons.length)];
    }

}
