package com.controleazulpessoal.finance_api.usecase.auth.output;

import lombok.Builder;

@Builder
public record LoginResponse(String token) {
}
