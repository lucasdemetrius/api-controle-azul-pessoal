package com.controleazulpessoal.finance_api.usecase.user;

import com.controleazulpessoal.finance_api.exception.user.UserNotAuthenticatedException;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.usecase.user.output.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GetAuthenticatedUserUseCase {

    @Value("${aws.cloudfront.url}")
    private String cloudFrontUrl;

    public UserResponse execute(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()){
            throw new UserNotAuthenticatedException();
        }

        User user = (User) authentication.getPrincipal();

        String fullPath = null;
        if (user.getImageProfile() != null) {
            fullPath = cloudFrontUrl + "/" + user.getImageProfile();
        }

        assert user != null;
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(fullPath)
                .build();
    }

}
