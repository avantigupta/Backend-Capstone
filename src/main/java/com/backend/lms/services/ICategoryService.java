package com.backend.lms.services;

import com.backend.lms.dto.categoryDto.CategoryDTO;
import com.backend.lms.entities.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {

    List<CategoryDTO> fetchCategoriesAll();

    String saveCategories(List<CategoryDTO> categoryDTOs);

    Long getCategoryCount();

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    Page<Category> getCategories(int page , int size, String search);

}
