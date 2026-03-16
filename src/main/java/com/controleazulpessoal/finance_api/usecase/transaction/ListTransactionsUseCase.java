package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.infrastructure.security.AuthenticatedUserProvider;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.TransactionRepository;
import com.controleazulpessoal.finance_api.usecase.transaction.mapper.TransactionMapper;
import com.controleazulpessoal.finance_api.usecase.transaction.output.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListTransactionsUseCase {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final AuthenticatedUserProvider authProvider;

    public Page<TransactionDto> execute(Pageable pageable) {
        User user = authProvider.getAuthenticatedUser();

        return repository.findAllByUserOrderByTransactionDateDesc(user, pageable)
                .map(mapper::entityToDto);
    }
}