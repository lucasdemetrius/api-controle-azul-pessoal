package com.controleazulpessoal.finance_api.persistence.repository;

import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findAllByUser(User user);

    boolean existsByNameAndUser(String name, User user);
}