package com.controleazulpessoal.finance_api.usecase.auth;

import com.controleazulpessoal.finance_api.exception.auth.RefreshTokenInvalidException;
import com.controleazulpessoal.finance_api.persistence.entity.RefreshToken;
import com.controleazulpessoal.finance_api.persistence.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void execute(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> {
                    log.warn("Logout attempt with invalid token");
                    return new RefreshTokenInvalidException();
                });

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        log.info("User logged out successfully. user: {}", refreshToken.getUser().getId());
    }
}