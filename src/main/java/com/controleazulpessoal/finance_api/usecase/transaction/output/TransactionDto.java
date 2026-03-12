package com.controleazulpessoal.finance_api.usecase.transaction.output;

import com.controleazulpessoal.finance_api.persistence.enums.TransactionType;
import com.controleazulpessoal.finance_api.usecase.category.output.CategoryDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDto(
        UUID id,
        BigDecimal amount,
        String description,
        LocalDateTime transactionDate,
        TransactionType type,
        boolean isPaid,
        CategoryDto category,
        boolean isFixed,
        boolean isRecurring,
        String attachmentUrl
) {}
