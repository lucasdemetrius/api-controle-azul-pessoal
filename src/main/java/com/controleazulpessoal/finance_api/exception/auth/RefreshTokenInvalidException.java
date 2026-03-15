package com.controleazulpessoal.finance_api.exception.auth;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class RefreshTokenInvalidException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "REFRESH_TOKEN_INVALID";

    public RefreshTokenInvalidException() {
        super("Refresh token is invalid or has been revoked.");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}