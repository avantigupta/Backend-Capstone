package com.backend.lms.services.Impl;

import com.backend.lms.dto.categoryDto.CategoryDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Category;
import com.backend.lms.exception.MethodNotAllowedException;
import com.backend.lms.exception.ResourceConflictException;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.CategoryMapper;
import com.backend.lms.repositories.BooksRepository;
import com.backend.lms.repositories.CategoryRepository;
import com.backend.lms.repositories.IssuanceRepository;
import com.backend.lms.services.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImp implements ICategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final BooksRepository booksRepository;

    @Autowired
    private final IssuanceRepository issuanceRepository;

    @Override
    public List<CategoryDTO> fetchCategoriesAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        categories.forEach(category ->
                categoryDTOList.add(CategoryMapper.mapToCategoryDTO(category))
        );
        return categoryDTOList;
    }

    @Override
    public Page<Category> getCategories(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "id"));
        if (search != null && !search.isEmpty()) {
          return categoryRepository.findByCategoryNameContainingIgnoreCase(search, pageable);
        } else {
            return categoryRepository.findAll(pageable);
        }
    }

    @Override
    public String saveCategories(List<CategoryDTO> categoryDTOList) {
        List<Category> categories = new ArrayList<>();
        categoryDTOList.forEach(categoryDTO -> {
            if (categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())) {
                throw new ResourceConflictException("Category already exists with name:  " + categoryDTO.getCategoryName());
            }
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

    Optional<Category> conflictingCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
    if (conflictingCategory.isPresent() && !conflictingCategory.get().getId().equals(id)) {
        throw new ResourceConflictException("Category with name '" + categoryDTO.getCategoryName() + "' already exists.");
    }
    existingCategory.setCategoryName(categoryDTO.getCategoryName());
   Category updatedCategory = categoryRepository.save(existingCategory);
   return CategoryMapper.mapToCategoryDTO(updatedCategory);
}
    @Transactional
    @Override
    public String deleteCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()) {
            throw new ResourceNotFoundException("Category not found with ID: " + id);
        }

        Category category = categoryOptional.get();
        List<Books> booksInCategory = booksRepository.findByCategoryId(id);

        if (booksInCategory.isEmpty()) {
            categoryRepository.deleteById(id);
            return "Category deleted successfully";
        }
        boolean hasIssuedBooks = booksInCategory.stream()
                .anyMatch(book -> issuanceRepository.existsByBookIdAndStatus(book.getId(), "ISSUED"));

        if (hasIssuedBooks) {
            throw new MethodNotAllowedException("Category cannot be deleted as some books under this category are currently issued.");
        }
        booksInCategory.forEach(book -> issuanceRepository.deleteByBookId(book.getId())); // Remove all issuances for these books
        booksRepository.deleteAll(booksInCategory); // Remove all books from the category
        categoryRepository.deleteById(id); // Remove the category
        return "Category and all related books deleted successfully";
    }
}

