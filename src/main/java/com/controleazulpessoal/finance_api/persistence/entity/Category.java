package com.controleazulpessoal.finance_api.persistence.entity;

import com.controleazulpessoal.finance_api.exception.ForbiddenActionException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE categories SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public boolean isOwnedBy(User user) {
        return this.user.getId().equals(user.getId());
    }

    public void validateOwnership(User user) {
        if (!isOwnedBy(user)) {
            throw new ForbiddenActionException("You don't have permission to perform this action.");
        }
    }

    public void update(String name, String description, String color, String icon) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (color != null) this.color = color;
        if (icon != null) this.icon = icon;
    }
}