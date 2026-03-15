package com.controleazulpessoal.finance_api.controller.v1.auth.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token is required.")
        String refreshToken
) {
}