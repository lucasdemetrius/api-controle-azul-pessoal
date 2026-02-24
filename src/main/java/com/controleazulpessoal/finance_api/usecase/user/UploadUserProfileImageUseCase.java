package com.controleazulpessoal.finance_api.usecase.user;

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

    public UserResponse execute(MultipartFile file) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getImageProfile() != null && !currentUser.getImageProfile().isEmpty()) {
            s3StorageService.deleteFile(currentUser.getImageProfile());
        }

        String newFileKey = s3StorageService.uploadFile(file);

        currentUser.setImageProfile(newFileKey);
        userRepository.save(currentUser);

        return UserResponse.builder()
                .id(currentUser.getId())
                .name(currentUser.getName())
                .email(currentUser.getEmail())
                .profileImageUrl(newFileKey)
                .build();
    }
}
