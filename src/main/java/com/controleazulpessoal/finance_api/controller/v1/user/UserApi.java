package com.controleazulpessoal.finance_api.controller.v1.user;

import com.controleazulpessoal.finance_api.controller.v1.user.request.CreateUserRequest;
import com.controleazulpessoal.finance_api.usecase.user.output.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "User", description = "User related operations")
@RequestMapping(path = "/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public interface UserApi {

    @PostMapping
    @Operation(description = "Create new user")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<UserDto> create(@RequestBody @Valid CreateUserRequest request);

}
