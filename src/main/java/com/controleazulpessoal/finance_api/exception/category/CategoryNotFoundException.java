package com.controleazulpessoal.finance_api.exception.category;

import com.controleazulpessoal.finance_api.exception.HasErrorCode;

public class CategoryNotFoundException extends RuntimeException implements HasErrorCode {
    private static final String ERROR_CODE = "CATEGORY_NOT_FOUND";

    public CategoryNotFoundException() {
        super("Category not found.");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}