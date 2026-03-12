package com.controleazulpessoal.finance_api.controller.v1.auth;

import com.controleazulpessoal.finance_api.controller.v1.auth.request.LoginRequest;
import com.controleazulpessoal.finance_api.usecase.auth.AuthenticateUserUseCase;
import com.controleazulpessoal.finance_api.usecase.auth.output.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        String token = authenticateUserUseCase.execute(request.email(), request.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}