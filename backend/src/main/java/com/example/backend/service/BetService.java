package com.example.backend.service;

import com.example.backend.Endpoints.dto.BetDto;
import com.example.backend.Endpoints.dto.ProductDto;
import com.example.backend.Entity.Bet;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.Exceptions.ValidationException;

import java.util.List;

/**
 * Service interface for managing bet operations.
 */
public interface BetService {

    /**
     * Creates a new bet based on the provided DTO.
     *
     * @param betDto The DTO containing information about the bet to be created.
     * @return The created bet entity.
     */
    public Bet create(BetDto betDto) throws ValidationException;

    /**
     * Updates an existing bet based on the provided DTO.
     *
     * @param betDto The DTO containing updated information about the bet.
     * @return The updated bet entity.
     */
    public Bet update(BetDto betDto) throws AuthorizationException;

    /**
     * Deletes a bet with the specified ID.
     *
     * @param id The ID of the bet to be deleted.
     */
    public void delete(Long id) throws AuthorizationException;

    /**
     * Retrieves all bets associated with a specific product.
     *
     * @param prodId The id of the product.
     * @return A list of Bet objects associated with the specified product.
     */
    public List<BetDto> getAllBetsForProduct(Long prodId);

    /**
     * Retrieves all bets associated with the current user.
     *
     * @return A list of BetDto objects associated with the current user.
     */
    public List<BetDto> getAllBetsForUser();


}
