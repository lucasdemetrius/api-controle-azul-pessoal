package com.controleazulpessoal.finance_api.persistence.entity;

import com.controleazulpessoal.finance_api.exception.ForbiddenActionException;
import com.controleazulpessoal.finance_api.persistence.enums.RecurrenceFrequency;
import com.controleazulpessoal.finance_api.persistence.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE transactions SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Transaction {

    @Id
    private UUID id;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    private boolean isPaid;

    private LocalDateTime paymentDate;

    private String attachmentUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private boolean isFixed;
    private boolean isRecurring;
    private Integer recurrenceCount;

    @Enumerated(EnumType.STRING)
    private RecurrenceFrequency frequency;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public boolean isOwnedBy(User user) {
        return this.user.getId().equals(user.getId());
    }

    public void validateOwnership(User user) {
        if (!isOwnedBy(user)) {
            throw new ForbiddenActionException("You don't have permission to perform this action.");
        }
    }

    public void update(BigDecimal amount, String description, LocalDateTime transactionDate,
                       TransactionType type, Integer recurrenceCount,
                       RecurrenceFrequency frequency, boolean isFixed, boolean isRecurring) {
        if (amount != null) this.amount = amount;
        if (description != null) this.description = description;
        if (transactionDate != null) this.transactionDate = transactionDate;
        if (type != null) this.type = type;
        if (recurrenceCount != null) this.recurrenceCount = recurrenceCount;
        if (frequency != null) this.frequency = frequency;
        this.isFixed = isFixed;
        this.isRecurring = isRecurring;
    }
}