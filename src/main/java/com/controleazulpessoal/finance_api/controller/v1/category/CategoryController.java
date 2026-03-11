package com.controleazulpessoal.finance_api.controller.v1.category;

import com.controleazulpessoal.finance_api.controller.v1.category.request.CategoryRequest;
import com.controleazulpessoal.finance_api.usecase.category.CreateCategoryUseCase;
import com.controleazulpessoal.finance_api.usecase.category.ListCategoriesUseCase;
import com.controleazulpessoal.finance_api.usecase.category.DeleteCategoryUseCase;
import com.controleazulpessoal.finance_api.usecase.category.UpdateCategoryUseCase;
import com.controleazulpessoal.finance_api.usecase.category.output.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;

    @Override
    public ResponseEntity<CategoryDto> create(CategoryRequest request) {
        return ResponseEntity.status(201).body(createCategoryUseCase.execute(request));
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getAll() {
        return ResponseEntity.ok(listCategoriesUseCase.execute());
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        deleteCategoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CategoryDto> update(UUID id, CategoryRequest request) {
        return ResponseEntity.ok(updateCategoryUseCase.execute(id, request));
    }
}