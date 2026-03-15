package com.controleazulpessoal.finance_api.controller.v1.auth;

import com.controleazulpessoal.finance_api.controller.v1.auth.request.LoginRequest;
import com.controleazulpessoal.finance_api.controller.v1.auth.request.RefreshTokenRequest;
import com.controleazulpessoal.finance_api.usecase.auth.AuthenticateUserUseCase;
import com.controleazulpessoal.finance_api.usecase.auth.LogoutUseCase;
import com.controleazulpessoal.finance_api.usecase.auth.RefreshTokenUseCase;
import com.controleazulpessoal.finance_api.usecase.auth.output.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        return ResponseEntity.ok(authenticateUserUseCase.execute(request.email(), request.password()));
    }

    @Override
    public ResponseEntity<LoginResponse> refresh(RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenUseCase.execute(request.refreshToken()));
    }

    @Override
    public ResponseEntity<Void> logout(RefreshTokenRequest request) {
        logoutUseCase.execute(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}