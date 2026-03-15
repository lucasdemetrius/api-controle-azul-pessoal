package com.controleazulpessoal.finance_api.persistence.repository;

import com.controleazulpessoal.finance_api.persistence.entity.RefreshToken;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    void deleteAllByUser(User user);
}