package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.TransactionRepository;
import com.controleazulpessoal.finance_api.usecase.transaction.mapper.TransactionMapper;
import com.controleazulpessoal.finance_api.usecase.transaction.output.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListTransactionsByPeriodUseCase {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    public Page<TransactionDto> execute(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Listing transactions for user: {} between {} and {}", user.getId(), start, end);

        Page<TransactionDto> result = repository
                .findAllByUserAndTransactionDateBetweenOrderByTransactionDateDesc(user, start, end, pageable)
                .map(mapper::entityToDto);

        log.info("Found {} transactions for user: {} in period", result.getTotalElements(), user.getId());
        return result;
    }
}