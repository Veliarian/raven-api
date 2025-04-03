package com.raven.api.auth.controllers;

import com.raven.api.auth.dto.JwtAuthenticationResponse;
import com.raven.api.auth.dto.SignInRequest;
import com.raven.api.auth.dto.SignUpRequest;
import com.raven.api.auth.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Registration")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Login")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @GetMapping("/oauth-success")
    public ResponseEntity<String> oauth2Success(@RequestParam String token) {
        return ResponseEntity.ok("Login successful. Your token: " + token);
    }
}
