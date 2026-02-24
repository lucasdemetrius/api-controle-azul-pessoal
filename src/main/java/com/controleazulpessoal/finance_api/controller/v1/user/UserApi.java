package com.controleazulpessoal.finance_api.controller.v1.user;

import com.controleazulpessoal.finance_api.controller.v1.user.request.CreateUserRequest;
import com.controleazulpessoal.finance_api.controller.v1.user.request.UpdateUserRequest;
import com.controleazulpessoal.finance_api.usecase.user.output.UserDto;
import com.controleazulpessoal.finance_api.usecase.user.output.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User related operations")
@RequestMapping(path = "/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public interface UserApi {

    @PostMapping
    @Operation(description = "Create new user")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<UserDto> create(@RequestBody @Valid CreateUserRequest request);

    @GetMapping("/me")
    @Operation(description = "Get authenticated user profile")
    ResponseEntity<UserResponse> getMe();

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update authenticated user profile")
    ResponseEntity<UserResponse> update(@RequestBody UpdateUserRequest request);

}
