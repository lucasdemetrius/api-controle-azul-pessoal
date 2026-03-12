package com.controleazulpessoal.finance_api.controller.v1.auth;

import com.controleazulpessoal.finance_api.controller.v1.auth.request.LoginRequest;
import com.controleazulpessoal.finance_api.usecase.auth.output.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "Authentication operations")
@RequestMapping(path = "/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public interface AuthApi {

    @PostMapping("/login")
    @Operation(description = "Authenticate user and return JWT token")
    ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request);
}