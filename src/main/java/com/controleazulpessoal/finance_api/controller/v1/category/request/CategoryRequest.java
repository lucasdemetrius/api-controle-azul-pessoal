package com.controleazulpessoal.finance_api.controller.v1.category.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Name is required.")
    private String name;

    private String description;

    @NotBlank(message = "Color is required.")
    private String color;

    @NotBlank(message = "Icon is required.")
    private String icon;
}