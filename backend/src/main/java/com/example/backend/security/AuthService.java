package com.example.backend.security;


import com.example.backend.Entity.ApplicationUser;

public interface AuthService {

    /**
     * Retrieves the ApplicationUser associated with the current authentication token.
     *
     * @return The ApplicationUser associated with the token.
     */
    ApplicationUser getUserFromToken();
}
