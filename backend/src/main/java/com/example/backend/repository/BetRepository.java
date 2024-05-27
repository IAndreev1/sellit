package com.example.backend.repository;

import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Bet;
import com.example.backend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {

    Bet getBetById(Long id);

    List<Bet> getBetsByProduct(Product product);

    List<Bet> getBetsByProductId(Long id);

    List<Bet> getBetsByUser(ApplicationUser user);
}
