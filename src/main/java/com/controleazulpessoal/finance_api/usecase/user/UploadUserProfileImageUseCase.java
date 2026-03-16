package com.controleazulpessoal.finance_api.usecase.user;

import com.controleazulpessoal.finance_api.infrastructure.security.AuthenticatedUserProvider;
import com.controleazulpessoal.finance_api.infrastructure.storage.FileValidator;
import com.controleazulpessoal.finance_api.infrastructure.storage.S3StorageService;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.UserRepository;
import com.controleazulpessoal.finance_api.usecase.user.output.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadUserProfileImageUseCase {

    private final UserRepository userRepository;
    private final S3StorageService s3StorageService;
    private final AuthenticatedUserProvider authProvider;

    public UserResponse execute(MultipartFile file) {
        FileValidator.validateImage(file);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = authProvider.getAuthenticatedUser();

        if (user.getImageProfile() != null && !user.getImageProfile().isEmpty()) {
            s3StorageService.deleteFile(user.getImageProfile());
        }

        String newFileKey = s3StorageService.uploadFile(file, "profiles");

        user.setImageProfile(newFileKey);
        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(newFileKey)
                .build();
    }
}
