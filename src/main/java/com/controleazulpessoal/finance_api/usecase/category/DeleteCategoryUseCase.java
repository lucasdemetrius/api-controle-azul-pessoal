package com.controleazulpessoal.finance_api.usecase.category;

import com.controleazulpessoal.finance_api.exception.ForbiddenActionException;
import com.controleazulpessoal.finance_api.exception.category.CategoryNotFoundException;
import com.controleazulpessoal.finance_api.infrastructure.security.AuthenticatedUserProvider;
import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.CategoryRepository;
import com.controleazulpessoal.finance_api.persistence.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserProvider authProvider;

    @Transactional
    public void execute(UUID id) {
        User user = authProvider.getAuthenticatedUser();

        log.info("Soft deleting category: {} for user: {}", id, user.getId());

        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        if (!category.getUser().getId().equals(user.getId())) {
            log.warn("User: {} attempted to delete category: {} owned by another user", user.getId(), id);
            throw new ForbiddenActionException("You don't have permission to delete this category.");
        }

        if (transactionRepository.existsByCategory(category)) {
            throw new ForbiddenActionException("Cannot delete category with existing transactions.");
        }

        categoryRepository.delete(category);
        log.info("Category soft deleted successfully. id: {}", id);
    }
}