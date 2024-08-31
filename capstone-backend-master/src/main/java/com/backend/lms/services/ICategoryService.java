package com.backend.lms.services;

import com.backend.lms.dto.categoryDto.CategoryDTO;
import java.util.List;

public interface ICategoryService {

    List<CategoryDTO> fetchCategories();

    String saveCategories(List<CategoryDTO> categoryDTOs);

    Long getCategoryCount();

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);
}
