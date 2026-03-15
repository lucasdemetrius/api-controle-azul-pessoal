package com.controleazulpessoal.finance_api.usecase.auth;

import com.controleazulpessoal.finance_api.exception.auth.RefreshTokenExpiredException;
import com.controleazulpessoal.finance_api.exception.auth.RefreshTokenInvalidException;
import com.controleazulpessoal.finance_api.infrastructure.security.TokenService;
import com.controleazulpessoal.finance_api.persistence.entity.RefreshToken;
import com.controleazulpessoal.finance_api.persistence.repository.RefreshTokenRepository;
import com.controleazulpessoal.finance_api.usecase.auth.output.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    @Transactional
    public LoginResponse execute(String refreshTokenValue) {
        log.info("Refresh token attempt");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> {
                    log.warn("Refresh token not found");
                    return new RefreshTokenInvalidException();
                });

        if (refreshToken.isRevoked()) {
            log.warn("Refresh token already revoked for user: {}", refreshToken.getUser().getId());
            throw new RefreshTokenInvalidException();
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Refresh token expired for user: {}", refreshToken.getUser().getId());
            throw new RefreshTokenExpiredException();
        }

        // Revoga o token atual e gera um novo par
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        String newAccessToken = tokenService.generateToken(refreshToken.getUser());
        String newRefreshToken = tokenService.generateRefreshToken(refreshToken.getUser());

        RefreshToken savedRefreshToken = RefreshToken.builder()
                .token(newRefreshToken)
                .user(refreshToken.getUser())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        refreshTokenRepository.save(savedRefreshToken);

        log.info("Tokens refreshed successfully for user: {}", refreshToken.getUser().getId());
        return new LoginResponse(newAccessToken, newRefreshToken);
    }
}