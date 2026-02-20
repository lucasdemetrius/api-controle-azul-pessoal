package com.controleazulpessoal.finance_api.persistence.repository;

import com.controleazulpessoal.finance_api.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Para o SecurityFilter (retorno direto)
    UserDetails findByEmail(String email);

    // Para os UseCases (retorno com Optional para usar o orElseThrow)
    // Note que mudamos o nome para evitar conflito de assinatura
    Optional<User> findOptionalByEmail(String email);

    boolean existsByEmail(String email);
}
