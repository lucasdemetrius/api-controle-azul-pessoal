package com.controleazulpessoal.finance_api.controller.v1.auth.request;

import lombok.Builder;

@Builder
public record LoginRequest(
        String email,
        String password) {
}
