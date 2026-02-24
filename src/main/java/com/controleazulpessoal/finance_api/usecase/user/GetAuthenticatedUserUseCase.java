package com.controleazulpessoal.finance_api.usecase.user;

import com.controleazulpessoal.finance_api.exception.user.UserNotAuthenticatedException;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.usecase.user.output.UserResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GetAuthenticatedUserUseCase {

    public UserResponse execute(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()){
            throw new UserNotAuthenticatedException();
        }

        User user = (User) authentication.getPrincipal();

        assert user != null;
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(user.getImageProfile())
                .build();
    }

}
