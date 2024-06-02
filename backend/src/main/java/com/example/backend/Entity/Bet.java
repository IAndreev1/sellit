package com.example.backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Double amount;

    private LocalDate date;

    private boolean accepted;

    private boolean rejected;
    @ManyToOne
    private ApplicationUser user;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public Product getProduct() {
        return product;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }
}
