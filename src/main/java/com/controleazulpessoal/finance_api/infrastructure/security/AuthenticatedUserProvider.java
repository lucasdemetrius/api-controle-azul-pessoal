package com.controleazulpessoal.finance_api.infrastructure.security;

import com.controleazulpessoal.finance_api.exception.user.UserNotAuthenticatedException;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserProvider {

    public User getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthenticatedException();
        }

        return (User) authentication.getPrincipal();
    }
}