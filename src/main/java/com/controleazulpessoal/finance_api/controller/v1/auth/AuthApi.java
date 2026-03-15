package com.controleazulpessoal.finance_api.controller.v1.auth;

import com.controleazulpessoal.finance_api.controller.v1.auth.request.LoginRequest;
import com.controleazulpessoal.finance_api.controller.v1.auth.request.RefreshTokenRequest;
import com.controleazulpessoal.finance_api.usecase.auth.output.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Auth", description = "Authentication operations")
@RequestMapping(path = "/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public interface AuthApi {

    @PostMapping("/login")
    @Operation(description = "Authenticate user and return JWT token")
    ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request);

    @PostMapping("/refresh")
    @Operation(description = "Refresh access token using refresh token")
    ResponseEntity<LoginResponse> refresh(@RequestBody @Valid RefreshTokenRequest request);

    @PostMapping("/logout")
    @Operation(description = "Logout user and revoke refresh token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequest request);
}