package com.controleazulpessoal.finance_api.controller.v1.category;

import com.controleazulpessoal.finance_api.controller.v1.category.request.CategoryRequest;
import com.controleazulpessoal.finance_api.controller.v1.category.request.UpdateCategoryRequest;
import com.controleazulpessoal.finance_api.response.Response;
import com.controleazulpessoal.finance_api.usecase.category.CreateCategoryUseCase;
import com.controleazulpessoal.finance_api.usecase.category.ListCategoriesUseCase;
import com.controleazulpessoal.finance_api.usecase.category.DeleteCategoryUseCase;
import com.controleazulpessoal.finance_api.usecase.category.UpdateCategoryUseCase;
import com.controleazulpessoal.finance_api.usecase.category.output.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;

    @Override
    public ResponseEntity<Response<CategoryDto>> create(CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.of(createCategoryUseCase.execute(request)));
    }

    @Override
    public ResponseEntity<Response<Page<CategoryDto>>> getAll(Pageable pageable) {
        return ResponseEntity.ok(Response.of(listCategoriesUseCase.execute(pageable)));
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        deleteCategoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Response<CategoryDto>> update(UUID id, UpdateCategoryRequest request) {
        return ResponseEntity.ok(Response.of(updateCategoryUseCase.execute(id, request)));
    }
}