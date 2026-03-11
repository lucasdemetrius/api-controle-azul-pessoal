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

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDto execute(CategoryRequest request) {
        // 1. Recupera o usuário logado do contexto de segurança
        // O seu SecurityFilter coloca o objeto User lá
        User authenticatedUser = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // 2. Validação opcional: Evitar duplicidade de nome para o mesmo usuário
        if (categoryRepository.existsByNameAndUser(request.getName(), authenticatedUser)) {
            throw new RuntimeException("Category with this name already exists for this user.");
        }

        // 3. Mapeia Request para Entity e associa o usuário
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .color(request.getColor())
                .icon(request.getIcon())
                .user(authenticatedUser)
                .build();

        // 4. Salva e retorna o DTO
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.entityToDto(savedCategory);
    }
}