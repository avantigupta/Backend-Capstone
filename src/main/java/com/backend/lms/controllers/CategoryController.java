package com.backend.lms.controllers;

import com.backend.lms.dto.ResponseDto.ResponseDTO;
import com.backend.lms.dto.categoryDto.CategoryDTO;
import com.backend.lms.entities.Category;
import com.backend.lms.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.backend.lms.constants.constants.*;


@RestController
@RequestMapping(value = "api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> fetchCategories() {
        List<CategoryDTO> categories = iCategoryService.fetchCategoriesAll();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<Page<Category>> getCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "search", required = false) String search
    ){
        Page<Category> categories = iCategoryService.getCategories(page, size, search);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }


    @PostMapping("/save")
    @CrossOrigin
    public ResponseEntity<ResponseDTO> createCategories(@RequestBody List<CategoryDTO> categoryDTOList) {
        iCategoryService.saveCategories(categoryDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, CREATE_MESSAGE));
    }


    @GetMapping("/count")
    public ResponseEntity<Long> getCategoryCount() {
        Long categoryCount = iCategoryService.getCategoryCount();
        return ResponseEntity.status(HttpStatus.OK).body(categoryCount);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteCategory(@PathVariable Long id) {
        iCategoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, DELETE_MESSAGE));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategoryDTO = iCategoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, UPDATE_MESSAGE));
    }
}