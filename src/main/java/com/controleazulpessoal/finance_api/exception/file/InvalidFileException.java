package com.controleazulpessoal.finance_api.exception.file;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class InvalidFileException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "INVALID_FILE";

    public InvalidFileException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}