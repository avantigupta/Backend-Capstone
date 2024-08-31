package com.backend.lms.controllers;

import com.backend.lms.dto.ResponseDto.ResponseDTO;
import com.backend.lms.dto.categoryDto.CategoryDTO;
import com.backend.lms.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> fetchCategories() {
        List<CategoryDTO> categories = iCategoryService.fetchCategories();

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PostMapping("/save")
    @CrossOrigin
    public ResponseEntity<String> createCategories(@RequestBody List<CategoryDTO> categoryDTOList) {
        String message = iCategoryService.saveCategories(categoryDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(message);
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
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("200","Deleted succesfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategoryDTO = iCategoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategoryDTO);
    }




}