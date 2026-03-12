package com.controleazulpessoal.finance_api.usecase.category;

import com.controleazulpessoal.finance_api.controller.v1.category.request.CategoryRequest;
import com.controleazulpessoal.finance_api.controller.v1.category.request.UpdateCategoryRequest;
import com.controleazulpessoal.finance_api.exception.ForbiddenActionException;
import com.controleazulpessoal.finance_api.exception.category.CategoryNotFoundException;
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
    public CategoryDto execute(UUID id, UpdateCategoryRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        if (!category.getUser().getId().equals(user.getId())) {
            throw new ForbiddenActionException("You don't have permission to update this category.");
        }

        if (request.name() != null) category.setName(request.name());
        if (request.description() != null) category.setDescription(request.description());
        if (request.color() != null) category.setColor(request.color());
        if (request.icon() != null) category.setIcon(request.icon());

        return categoryMapper.entityToDto(categoryRepository.save(category));
    }
}