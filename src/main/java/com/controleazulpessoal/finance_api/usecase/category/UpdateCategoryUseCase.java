package com.controleazulpessoal.finance_api.usecase.category;

import com.controleazulpessoal.finance_api.controller.v1.category.request.CategoryRequest;
import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.CategoryRepository;
import com.controleazulpessoal.finance_api.usecase.category.mapper.CategoryMapper;
import com.controleazulpessoal.finance_api.usecase.category.output.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDto execute(UUID id, CategoryRequest request) {
        User authenticatedUser = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));

        // Segurança: Impede que um usuário edite a categoria de outro
        if (!category.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("You don't have permission to update this category.");
        }

        // Atualiza os campos
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());

        return categoryMapper.entityToDto(categoryRepository.save(category));
    }
}