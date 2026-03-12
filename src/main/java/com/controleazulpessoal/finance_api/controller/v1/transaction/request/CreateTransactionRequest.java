package com.controleazulpessoal.finance_api.controller.v1.transaction.request;

import com.controleazulpessoal.finance_api.persistence.enums.RecurrenceFrequency;
import com.controleazulpessoal.finance_api.persistence.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {

    private UUID id;

    @NotNull(message = "Amount is required.")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "Transaction date is required.")
    private LocalDateTime transactionDate;

    @NotNull(message = "Type is required.")
    private TransactionType type;

    @NotNull(message = "Category ID is required.")
    private UUID categoryId;

    private boolean isFixed;

    private boolean isRecurring;

    private Integer recurrenceCount;

    private RecurrenceFrequency frequency;
}