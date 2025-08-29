package com.omardev.blog.controllers;

import com.omardev.blog.domain.dtos.auth.AuthenticationResponse;
import com.omardev.blog.domain.dtos.auth.LoginRequest;
import com.omardev.blog.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling authentication requests.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Authenticates the user and returns a JWT.
     *
     * @param loginRequest user credentials (email and password)
     * @return authentication response with the JWT
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthenticationResponse authResponse = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(authResponse);
    }
}
