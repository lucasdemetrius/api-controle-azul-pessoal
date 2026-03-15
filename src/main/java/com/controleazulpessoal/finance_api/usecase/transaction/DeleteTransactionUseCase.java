package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.exception.transaction.TransactionAccessDeniedException;
import com.controleazulpessoal.finance_api.exception.transaction.TransactionNotFoundException;
import com.controleazulpessoal.finance_api.persistence.entity.Transaction;
import com.controleazulpessoal.finance_api.persistence.entity.User;
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
public class DeleteTransactionUseCase {
    private final TransactionRepository repository;

    @Transactional
    public void execute(UUID id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Deleting transaction: {} for user: {}", id, user.getId());

        Transaction transaction = repository.findById(id)
                .orElseThrow(TransactionNotFoundException::new);

        if (!transaction.getUser().getId().equals(user.getId())) {
            log.warn("User: {} attempted to delete transaction: {} owned by another user", user.getId(), id);
            throw new TransactionAccessDeniedException();
        }

        repository.delete(transaction);
        log.info("Transaction deleted successfully. id: {}", id);
    }
}