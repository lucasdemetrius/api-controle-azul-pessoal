package com.controleazulpessoal.finance_api.usecase.user;

import com.controleazulpessoal.finance_api.controller.v1.user.request.UpdateUserRequest;
import com.controleazulpessoal.finance_api.infrastructure.security.AuthenticatedUserProvider;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.UserRepository;
import com.controleazulpessoal.finance_api.usecase.user.output.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final AuthenticatedUserProvider authProvider;

    public UserResponse execute(UpdateUserRequest request){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = authProvider.getAuthenticatedUser();

        if (request.name() != null){
            user.setName(request.name());
        }

        if (request.phoneNumber() != null) {
            user.setPhoneNumber(request.phoneNumber());
        }

        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(user.getImageProfile())
                .build();
    }

}
