package com.controleazulpessoal.finance_api.usecase.category;

import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void execute(UUID id) {
        User authenticatedUser = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Busca a categoria e verifica se existe
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));

        // Segurança: Verifica se o usuário logado é o dono da categoria
        if (!category.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("You don't have permission to delete this category.");
        }

        categoryRepository.delete(category);
    }
}