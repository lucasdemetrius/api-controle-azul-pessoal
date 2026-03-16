package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.controller.v1.transaction.request.UpdateTransactionRequest;
import com.controleazulpessoal.finance_api.exception.category.CategoryNotFoundException;
import com.controleazulpessoal.finance_api.exception.transaction.TransactionNotFoundException;
import com.controleazulpessoal.finance_api.infrastructure.security.AuthenticatedUserProvider;
import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.Transaction;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.CategoryRepository;
import com.controleazulpessoal.finance_api.persistence.repository.TransactionRepository;
import com.controleazulpessoal.finance_api.usecase.transaction.mapper.TransactionMapper;
import com.controleazulpessoal.finance_api.usecase.transaction.output.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateTransactionUseCase {

    private final TransactionRepository repository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper mapper;
    private final AuthenticatedUserProvider authProvider;

    @Transactional
    public TransactionDto execute(UUID id, UpdateTransactionRequest request) {
        User user = authProvider.getAuthenticatedUser();
        log.info("Updating transaction: {} for user: {}", id, user.getId());

        Transaction transaction = repository.findById(id)
                .orElseThrow(TransactionNotFoundException::new);

        transaction.validateOwnership(user);
        transaction.update(
                request.amount(),
                request.description(),
                request.transactionDate(),
                request.type(),
                request.recurrenceCount(),
                request.frequency(),
                request.isFixed(),
                request.isRecurring()
        );

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(CategoryNotFoundException::new);

            category.validateOwnership(user);
            transaction.setCategory(category);
            log.info("Category updated to: {} for transaction: {}", request.categoryId(), id);
        }

        TransactionDto result = mapper.entityToDto(repository.save(transaction));
        log.info("Transaction updated successfully. id: {}", id);
        return result;
    }
}