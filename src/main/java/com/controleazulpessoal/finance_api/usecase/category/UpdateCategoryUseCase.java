package com.controleazulpessoal.finance_api.usecase.category;

import com.controleazulpessoal.finance_api.controller.v1.category.request.UpdateCategoryRequest;
import com.controleazulpessoal.finance_api.exception.ForbiddenActionException;
import com.controleazulpessoal.finance_api.exception.category.CategoryNotFoundException;
import com.controleazulpessoal.finance_api.infrastructure.security.AuthenticatedUserProvider;
import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.CategoryRepository;
import com.controleazulpessoal.finance_api.usecase.category.mapper.CategoryMapper;
import com.controleazulpessoal.finance_api.usecase.category.output.CategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuthenticatedUserProvider authProvider;

    @Transactional
    public CategoryDto execute(UUID id, UpdateCategoryRequest request) {
        User user = authProvider.getAuthenticatedUser();
        log.info("Updating category: {} for user: {}", id, user.getId());

        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        category.validateOwnership(user);
        category.update(
                request.name(),
                request.description(),
                request.color(),
                request.icon()
        );

        CategoryDto result = categoryMapper.entityToDto(categoryRepository.save(category));
        log.info("Category updated successfully. id: {}", id);
        return result;
    }
}