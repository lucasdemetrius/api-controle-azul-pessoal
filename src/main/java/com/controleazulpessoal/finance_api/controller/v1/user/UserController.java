package com.controleazulpessoal.finance_api.controller.v1.user;

import com.controleazulpessoal.finance_api.controller.v1.user.request.CreateUserRequest;
import com.controleazulpessoal.finance_api.controller.v1.user.request.UpdateUserRequest;
import com.controleazulpessoal.finance_api.usecase.user.CreateUserUseCase;
import com.controleazulpessoal.finance_api.usecase.user.GetAuthenticatedUserUseCase;
import com.controleazulpessoal.finance_api.usecase.user.UpdateUserUseCase;
import com.controleazulpessoal.finance_api.usecase.user.UploadUserProfileImageUseCase;
import com.controleazulpessoal.finance_api.usecase.user.output.UserDto;
import com.controleazulpessoal.finance_api.usecase.user.output.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final CreateUserUseCase createUserUseCase;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UploadUserProfileImageUseCase uploadUserProfileImageUseCase;

    @Override
    public ResponseEntity<UserDto> create(CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserUseCase.executeAndNotify(request));
    }

    @Override
    public ResponseEntity<UserResponse> getMe() {
        return ResponseEntity.ok(getAuthenticatedUserUseCase.execute());
    }

    @Override
    public ResponseEntity<UserResponse> update(UpdateUserRequest request) {
        return ResponseEntity.ok(updateUserUseCase.execute(request));
    }

    @Override
    public ResponseEntity<UserResponse> uploadProfileImage(MultipartFile file) {
        return ResponseEntity.ok(uploadUserProfileImageUseCase.execute(file));
    }


}
