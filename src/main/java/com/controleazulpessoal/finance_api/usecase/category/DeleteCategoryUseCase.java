package com.controleazulpessoal.finance_api.usecase.category;

import com.controleazulpessoal.finance_api.exception.ForbiddenActionException;
import com.controleazulpessoal.finance_api.exception.category.CategoryNotFoundException;
import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.CategoryRepository;
import com.controleazulpessoal.finance_api.persistence.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void execute(UUID id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        if (!category.getUser().getId().equals(user.getId())) {
            throw new ForbiddenActionException("You don't have permission to delete this category.");
        }

        if (transactionRepository.existsByCategory(category)) {
            throw new ForbiddenActionException("Cannot delete category with existing transactions.");
        }

        categoryRepository.delete(category);
    }
}