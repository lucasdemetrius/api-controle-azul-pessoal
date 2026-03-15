package com.controleazulpessoal.finance_api.usecase.auth;

import com.controleazulpessoal.finance_api.exception.user.UserNotAuthenticatedException;
import com.controleazulpessoal.finance_api.exception.user.UserNotFoundException;
import com.controleazulpessoal.finance_api.infrastructure.security.TokenService;
import com.controleazulpessoal.finance_api.persistence.repository.UserRepository;
import com.controleazulpessoal.finance_api.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public String execute(String email, String senhaPura) {
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

        log.info("Login successful for user: {}", user.getId());
        return tokenService.generateToken(user);
    }
}