package com.controleazulpessoal.finance_api.exception.user;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class UserNotFoundException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "USER_NOT_FOUND";

    public UserNotFoundException() {
        super("user-not-found");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
