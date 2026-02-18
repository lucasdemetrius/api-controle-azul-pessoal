package com.controleazulpessoal.finance_api.controller.v1.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    private String name;

    private String phoneNumber;

    private Boolean receiveNotifications;

}
