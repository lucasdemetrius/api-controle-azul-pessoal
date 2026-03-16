package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.controller.v1.transaction.request.UpdateTransactionRequest;
import com.controleazulpessoal.finance_api.exception.ForbiddenActionException;
import com.controleazulpessoal.finance_api.exception.category.CategoryNotFoundException;
import com.controleazulpessoal.finance_api.exception.transaction.TransactionAccessDeniedException;
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
import org.springframework.security.core.context.SecurityContextHolder;
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

        if (!transaction.getUser().getId().equals(user.getId())) {
            log.warn("User: {} attempted to update transaction: {} owned by another user", user.getId(), id);
            throw new TransactionAccessDeniedException();
        }

        if (request.amount() != null) transaction.setAmount(request.amount());
        if (request.description() != null) transaction.setDescription(request.description());
        if (request.transactionDate() != null) transaction.setTransactionDate(request.transactionDate());
        if (request.type() != null) transaction.setType(request.type());
        if (request.recurrenceCount() != null) transaction.setRecurrenceCount(request.recurrenceCount());
        if (request.frequency() != null) transaction.setFrequency(request.frequency());

        transaction.setFixed(request.isFixed());
        transaction.setRecurring(request.isRecurring());

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(CategoryNotFoundException::new);

            if (!category.getUser().getId().equals(user.getId())) {
                log.warn("User: {} attempted to use category: {} owned by another user", user.getId(), request.categoryId());
                throw new ForbiddenActionException("You don't have permission to use this category.");
            }

            transaction.setCategory(category);
            log.info("Category updated to: {} for transaction: {}", request.categoryId(), id);
        }

        TransactionDto result = mapper.entityToDto(repository.save(transaction));
        log.info("Transaction updated successfully. id: {}", id);
        return result;
    }
}