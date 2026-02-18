package com.controleazulpessoal.finance_api.controller.v1.user;

import com.controleazulpessoal.finance_api.controller.v1.user.request.CreateUserRequest;
import com.controleazulpessoal.finance_api.usecase.user.CreateUserUseCase;
import com.controleazulpessoal.finance_api.usecase.user.output.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final CreateUserUseCase createUserUseCase;

    @Override
    public ResponseEntity<UserDto> create(CreateUserRequest request) {
        return ResponseEntity.ok(createUserUseCase.execute(request));
    }

}
