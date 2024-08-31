package com.backend.lms.services.Impl;

import com.backend.lms.dto.categoryDto.CategoryDTO;
import com.backend.lms.entities.Category;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.CategoryMapper;
import com.backend.lms.repositories.CategoryRepository;
import com.backend.lms.services.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImp implements ICategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;


    //Fetching all categories
    @Override
    public List<CategoryDTO> fetchCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        categories.forEach(category ->
                categoryDTOList.add(CategoryMapper.mapToCategoryDTO(category))
        );
        return categoryDTOList;
    }

    @Override
    public String saveCategories(List<CategoryDTO> categoryDTOList) {
        List<Category> categories = new ArrayList<>();

        categoryDTOList.forEach(categoryDTO -> {
            Category category = new Category();
            categories.add(CategoryMapper.maptoCategory(categoryDTO, category));
        });

        List<Category> savedCategories = categoryRepository.saveAll(categories);

        return savedCategories.size() + " Categories added successfully";
    }

    @Override
    public Long getCategoryCount() {
        return categoryRepository.count();
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        Category updatedCategory = CategoryMapper.maptoCategory(categoryDTO, existingCategory);

        updatedCategory = categoryRepository.save(updatedCategory);

        return CategoryMapper.mapToCategoryDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
