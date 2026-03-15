package com.controleazulpessoal.finance_api.persistence.repository;

import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.Transaction;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findAllByUserOrderByTransactionDateDesc(User user, Pageable pageable);

    Page<Transaction> findAllByUserAndTransactionDateBetweenOrderByTransactionDateDesc(
            User user, LocalDateTime start, LocalDateTime end, Pageable pageable);

    boolean existsByCategory(Category category);

}
