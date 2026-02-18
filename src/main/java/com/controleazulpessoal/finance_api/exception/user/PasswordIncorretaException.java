package com.controleazulpessoal.finance_api.exception.user;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class PasswordIncorretaException  extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "PASSWORD_AUTH_FAILED";

    public PasswordIncorretaException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
