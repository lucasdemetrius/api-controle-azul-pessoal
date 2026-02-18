package com.controleazulpessoal.finance_api.exception.user;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class UserNotAuthenticatedException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "USER_AUTH_FAILED";

    public UserNotAuthenticatedException() {
        super("Falha na autenticação. Verifique suas credenciais.");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
