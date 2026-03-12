package com.controleazulpessoal.finance_api.exception.category;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class CategoryAlreadyExistsException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "CATEGORY_ALREADY_EXISTS";

    public CategoryAlreadyExistsException() {
        super("Category with this name already exists for this user.");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}