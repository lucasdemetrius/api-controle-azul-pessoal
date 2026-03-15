package com.controleazulpessoal.finance_api.usecase.auth;

import com.controleazulpessoal.finance_api.exception.user.UserNotAuthenticatedException;
import com.controleazulpessoal.finance_api.exception.user.UserNotFoundException;
import com.controleazulpessoal.finance_api.infrastructure.security.TokenService;
import com.controleazulpessoal.finance_api.persistence.entity.RefreshToken;
import com.controleazulpessoal.finance_api.persistence.repository.RefreshTokenRepository;
import com.controleazulpessoal.finance_api.persistence.repository.UserRepository;
import com.controleazulpessoal.finance_api.usecase.auth.output.LoginResponse;
import com.controleazulpessoal.finance_api.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginResponse execute(String email, String senhaPura) {
        log.info("Login attempt for email: {}", email);

        var user = userRepository.findOptionalByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login failed — user not found for email: {}", email);
                    return new UserNotFoundException();
                });

        boolean isPasswordCorrect = PasswordUtils.verificarSenha(senhaPura, user.getPassword());
        if (!isPasswordCorrect) {
            log.warn("Login failed — incorrect password for email: {}", email);
            throw new UserNotAuthenticatedException();
        }

        // Revoga todos os refreshTokens anteriores do usuário
        refreshTokenRepository.deleteAllByUser(user);

        String accessToken = tokenService.generateToken(user);
        String refreshTokenValue = tokenService.generateRefreshToken(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        log.info("Login successful for user: {}", user.getId());
        return new LoginResponse(accessToken, refreshTokenValue);
    }
}