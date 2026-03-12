package com.controleazulpessoal.finance_api.exception.transaction;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class TransactionNotFoundException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "TRANSACTION_NOT_FOUND";

    public TransactionNotFoundException() {
        super("Transaction not found.");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}