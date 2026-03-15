package com.controleazulpessoal.finance_api.persistence.entity;

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
}