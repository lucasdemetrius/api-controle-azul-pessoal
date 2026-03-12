// TransactionAccessDeniedException.java
package com.controleazulpessoal.finance_api.exception.transaction;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class TransactionAccessDeniedException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "TRANSACTION_ACCESS_DENIED";

    public TransactionAccessDeniedException() {
        super("You don't have permission to access this transaction.");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}