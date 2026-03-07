package com.controleazulpessoal.finance_api.persistence.repository;

import com.controleazulpessoal.finance_api.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    UserDetails findByEmail(String email);

    Optional<User> findOptionalByEmail(String email);

    boolean existsByEmail(String email);

}
