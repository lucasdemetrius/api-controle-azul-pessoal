package com.controleazulpessoal.finance_api.controller.v1.category.request;

public record UpdateCategoryRequest(
        String name,
        String description,
        String color,
        String icon
) {
}