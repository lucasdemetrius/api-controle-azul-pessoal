package com.controleazulpessoal.finance_api.usecase.category;

import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.CategoryRepository;
import com.controleazulpessoal.finance_api.usecase.category.mapper.CategoryMapper;
import com.controleazulpessoal.finance_api.usecase.category.output.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListCategoriesUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> execute() {
        // Recupera o usuário autenticado para filtrar a busca
        User authenticatedUser = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Busca apenas as categorias pertencentes a este usuário
        return categoryRepository.findAllByUser(authenticatedUser)
                .stream()
                .map(categoryMapper::entityToDto)
                .collect(Collectors.toList());
    }
}