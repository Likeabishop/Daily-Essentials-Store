package com.pos.pos_backend.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.pos_backend.category.Category;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    List<Category> findByBusinessIdOrderByName(
            Long businessId
    );

    Optional<Category>
    findByIdAndBusinessId(
            Long id,
            Long businessId
    );
}