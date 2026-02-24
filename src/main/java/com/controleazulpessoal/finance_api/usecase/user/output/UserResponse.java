package com.controleazulpessoal.finance_api.usecase.user.output;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String name,
        String email,
        String profileImageUrl
) {
}
