package com.controleazulpessoal.finance_api.usecase.user;

import com.controleazulpessoal.finance_api.infrastructure.security.AuthenticatedUserProvider;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.usecase.user.output.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAuthenticatedUserUseCase {

    @Value("${aws.cloudfront.url}")
    private String cloudFrontUrl;

    private final AuthenticatedUserProvider authProvider;

    public UserResponse execute() {
        User user = authProvider.getAuthenticatedUser();

        String fullPath = null;
        if (user.getImageProfile() != null) {
            fullPath = cloudFrontUrl + "/" + user.getImageProfile();
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(fullPath)
                .build();
    }
}