package com.controleazulpessoal.finance_api.usecase.auth;

import com.controleazulpessoal.finance_api.exception.user.UserNotAuthenticatedException;
import com.controleazulpessoal.finance_api.exception.user.UserNotFoundException;
import com.controleazulpessoal.finance_api.infrastructure.security.TokenService;
import com.controleazulpessoal.finance_api.persistence.repository.UserRepository;
import com.controleazulpessoal.finance_api.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public String execute(String email, String senhaPura) {
        var user = userRepository.findOptionalByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        boolean isPasswordCorrect = PasswordUtils.verificarSenha(senhaPura, user.getPassword());
        if (!isPasswordCorrect) {
            throw new UserNotAuthenticatedException();
        }

        return tokenService.generateToken(user);
    }
}