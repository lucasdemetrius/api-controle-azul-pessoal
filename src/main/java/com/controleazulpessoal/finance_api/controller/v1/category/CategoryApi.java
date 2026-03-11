package com.controleazulpessoal.finance_api.controller.v1.category;

import com.controleazulpessoal.finance_api.controller.v1.category.request.CategoryRequest;
import com.controleazulpessoal.finance_api.usecase.category.output.CategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Category", description = "Finance categories management")
@RequestMapping(path = "/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public interface CategoryApi {

    @PostMapping
    @Operation(description = "Create a new category for the authenticated user")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<CategoryDto> create(@RequestBody @Valid CategoryRequest request);

    @GetMapping
    @Operation(description = "List all categories of the authenticated user")
    ResponseEntity<List<CategoryDto>> getAll();

    @DeleteMapping("/{id}")
    @Operation(description = "Delete a category")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    ResponseEntity<Void> delete(@PathVariable UUID id);

    @PutMapping("/{id}")
    @Operation(description = "Update an existing category")
    ResponseEntity<CategoryDto> update(@PathVariable UUID id, @RequestBody @Valid CategoryRequest request);
}