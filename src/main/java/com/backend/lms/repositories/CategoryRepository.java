package com.backend.lms.repositories;

import com.backend.lms.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCategoryName(String categoryName);

    Optional<Category> findByCategoryName(String categoryName);

    Optional<Category> findById(Long id);

    void deleteById(Long id);

    Page<Category> findByCategoryNameContainingIgnoreCase(String categoryName, Pageable pageable);
}