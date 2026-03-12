package com.controleazulpessoal.finance_api.exception;

public class ForbiddenActionException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "FORBIDDEN_ACTION";

    public ForbiddenActionException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}