package com.controleazulpessoal.finance_api.usecase.category.output;

import lombok.Builder;
import java.util.UUID;

@Builder
public record CategoryDto(
        UUID id,
        String name,
        String description,
        String color,
        String icon
) {
}