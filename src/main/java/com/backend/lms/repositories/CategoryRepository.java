package com.backend.lms.repositories;


import com.backend.lms.dto.categoryDto.CategoryDTO;
import com.backend.lms.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCategoryName(String categoryName);

    Page<Category> findByCategoryNameContainingIgnoreCase(String categoryName, Pageable pageable);
}