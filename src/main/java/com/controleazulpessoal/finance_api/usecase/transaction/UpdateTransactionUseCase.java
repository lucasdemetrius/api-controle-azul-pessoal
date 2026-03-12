package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.controller.v1.transaction.request.CreateTransactionRequest;
import com.controleazulpessoal.finance_api.persistence.entity.Transaction;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.TransactionRepository;
import com.controleazulpessoal.finance_api.usecase.transaction.mapper.TransactionMapper;
import com.controleazulpessoal.finance_api.usecase.transaction.output.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateTransactionUseCase {
    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    @Transactional
    public TransactionDto execute(UUID id, CreateTransactionRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Permission denied");
        }

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setType(request.getType());
        transaction.setFixed(request.isFixed());
        transaction.setRecurring(request.isRecurring());
        transaction.setRecurrenceCount(request.getRecurrenceCount());
        transaction.setFrequency(request.getFrequency());

        return mapper.entityToDto(repository.save(transaction));
    }
}