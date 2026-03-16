package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.exception.transaction.TransactionNotFoundException;
import com.controleazulpessoal.finance_api.infrastructure.security.AuthenticatedUserProvider;
import com.controleazulpessoal.finance_api.persistence.entity.Transaction;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteTransactionUseCase {

    private final TransactionRepository repository;
    private final AuthenticatedUserProvider authProvider;

    @Transactional
    public void execute(UUID id) {
        User user = authProvider.getAuthenticatedUser();

        log.info("Deleting transaction: {} for user: {}", id, user.getId());

        Transaction transaction = repository.findById(id)
                .orElseThrow(TransactionNotFoundException::new);

        transaction.validateOwnership(user);

        repository.delete(transaction);
        log.info("Transaction deleted successfully. id: {}", id);
    }
}