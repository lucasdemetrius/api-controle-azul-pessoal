package com.controleazulpessoal.finance_api.controller.v1.transaction.request;

import com.controleazulpessoal.finance_api.persistence.enums.RecurrenceFrequency;
import com.controleazulpessoal.finance_api.persistence.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateTransactionRequest(
        BigDecimal amount,
        String description,
        LocalDateTime transactionDate,
        TransactionType type,
        UUID categoryId,
        boolean isFixed,
        boolean isRecurring,
        Integer recurrenceCount,
        RecurrenceFrequency frequency
) {
}