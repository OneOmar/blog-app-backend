package com.omardev.blog.services;

import com.omardev.blog.domain.dtos.AuthenticationResponse;
import com.omardev.blog.domain.dtos.LoginRequest;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Service for handling authentication and JWT generation.
 */
public interface AuthenticationService {

    /**
     * Authenticate user and return a JWT response.
     *
     * @param request login request with credentials
     * @return authentication response containing JWT
     */
    AuthenticationResponse authenticate(LoginRequest request);

    /**
     * Generate a JWT token for the given user details.
     *
     * @param userDetails authenticated user details
     * @return JWT token
     */
    String generateToken(UserDetails userDetails);

    UserDetails validateToken(String token);
}
