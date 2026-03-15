package com.controleazulpessoal.finance_api.exception.auth;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class RefreshTokenExpiredException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "REFRESH_TOKEN_EXPIRED";

    public RefreshTokenExpiredException() {
        super("Refresh token has expired. Please login again.");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}