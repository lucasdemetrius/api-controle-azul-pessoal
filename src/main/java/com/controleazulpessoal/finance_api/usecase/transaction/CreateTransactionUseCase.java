package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.controller.v1.transaction.request.CreateTransactionRequest;
import com.controleazulpessoal.finance_api.exception.ForbiddenActionException;
import com.controleazulpessoal.finance_api.exception.category.CategoryNotFoundException;
import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.Transaction;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.enums.RecurrenceFrequency;
import com.controleazulpessoal.finance_api.persistence.repository.CategoryRepository;
import com.controleazulpessoal.finance_api.persistence.repository.TransactionRepository;
import com.controleazulpessoal.finance_api.usecase.transaction.mapper.TransactionMapper;
import com.controleazulpessoal.finance_api.usecase.transaction.output.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTransactionUseCase {
    private final TransactionRepository repository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper mapper;

    @Transactional
    public TransactionDto execute(CreateTransactionRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Creating transaction for user: {}, type: {}", user.getId(), request.getType());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        if (!category.getUser().getId().equals(user.getId())) {
            log.warn("User: {} attempted to use category: {} owned by another user", user.getId(), request.getCategoryId());
            throw new ForbiddenActionException("You don't have permission to use this category.");
        }

        Transaction mainTransaction = Transaction.builder()
                .id(request.getId() != null ? request.getId() : UUID.randomUUID())
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .type(request.getType())
                .user(user)
                .category(category)
                .isFixed(request.isFixed())
                .isRecurring(request.isRecurring())
                .recurrenceCount(request.getRecurrenceCount())
                .frequency(request.getFrequency())
                .build();

        Transaction saved = repository.save(mainTransaction);
        log.info("Transaction created successfully. id: {}, type: {}, amount: {}", saved.getId(), saved.getType(), saved.getAmount());

        if (request.isRecurring() && request.getRecurrenceCount() != null && request.getRecurrenceCount() > 1) {
            log.info("Generating {} recurring transactions for transaction: {}", request.getRecurrenceCount() - 1, saved.getId());
            generateRecurringTransactions(saved, request);
        }

        return mapper.entityToDto(saved);
    }

    private void generateRecurringTransactions(Transaction parent, CreateTransactionRequest request) {
        for (int i = 1; i < request.getRecurrenceCount(); i++) {
            LocalDateTime nextDate = calculateNextDate(parent.getTransactionDate(), request.getFrequency(), i);

            Transaction child = Transaction.builder()
                    .id(UUID.randomUUID())
                    .amount(parent.getAmount())
                    .description(parent.getDescription() + " (" + (i + 1) + "/" + request.getRecurrenceCount() + ")")
                    .transactionDate(nextDate)
                    .type(parent.getType())
                    .user(parent.getUser())
                    .category(parent.getCategory())
                    .isFixed(parent.isFixed())
                    .isRecurring(false)
                    .build();
            repository.save(child);
        }
        log.info("Recurring transactions generated successfully for parent: {}", parent.getId());
    }

    private LocalDateTime calculateNextDate(LocalDateTime baseDate, RecurrenceFrequency frequency, int increment) {
        return switch (frequency) {
            case DIARIO -> baseDate.plusDays(increment);
            case SEMANAL -> baseDate.plusWeeks(increment);
            case MENSAL -> baseDate.plusMonths(increment);
            case ANUAL -> baseDate.plusYears(increment);
        };
    }
}