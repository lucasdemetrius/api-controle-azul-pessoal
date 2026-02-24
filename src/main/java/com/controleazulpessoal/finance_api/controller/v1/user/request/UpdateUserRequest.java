package com.controleazulpessoal.finance_api.controller.v1.user.request;

public record UpdateUserRequest(
        String name,
        String phoneNumber
) {
}
