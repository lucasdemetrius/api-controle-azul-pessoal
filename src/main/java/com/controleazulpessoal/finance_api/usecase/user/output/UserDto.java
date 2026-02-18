package com.controleazulpessoal.finance_api.usecase.user.output;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        String email,
        String name,
        String phoneNumber,
        String imageProfile,
        Boolean receiveNotifications,
        LocalDateTime lastAccessDate,
        LocalDateTime accountCreationDate
) {
}
