package com.controleazulpessoal.finance_api.controller.v1.user;

import com.controleazulpessoal.finance_api.controller.v1.user.request.CreateUserRequest;
import com.controleazulpessoal.finance_api.controller.v1.user.request.UpdateUserRequest;
import com.controleazulpessoal.finance_api.usecase.user.CreateUserUseCase;
import com.controleazulpessoal.finance_api.usecase.user.GetAuthenticatedUserUseCase;
import com.controleazulpessoal.finance_api.usecase.user.UpdateUserUseCase;
import com.controleazulpessoal.finance_api.usecase.user.output.UserDto;
import com.controleazulpessoal.finance_api.usecase.user.output.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final CreateUserUseCase createUserUseCase;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    @Override
    public ResponseEntity<UserDto> create(CreateUserRequest request) {
        return ResponseEntity.ok(createUserUseCase.execute(request));
    }

    @Override
    public ResponseEntity<UserResponse> getMe() {
        return ResponseEntity.ok(getAuthenticatedUserUseCase.execute());
    }

    @Override
    public ResponseEntity<UserResponse> update(UpdateUserRequest request) {
        return ResponseEntity.ok(updateUserUseCase.execute(request));
    }

}
