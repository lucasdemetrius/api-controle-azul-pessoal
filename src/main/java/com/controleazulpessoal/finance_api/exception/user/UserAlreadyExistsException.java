package com.controleazulpessoal.finance_api.exception.user;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class UserAlreadyExistsException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "USER_ALREADY_EXISTS";

    public UserAlreadyExistsException() {
        super("user-already-exists");
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
