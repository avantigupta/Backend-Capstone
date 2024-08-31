package com.backend.lms.mapper;


import com.backend.lms.dto.categoryDto.CategoryDTO;
import com.backend.lms.entities.Category;

public class CategoryMapper {

    private CategoryMapper() {

    }


    public static CategoryDTO mapToCategoryDTO(Category category)
    {
        CategoryDTO categoryDTO=new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setCategoryName(category.getName());

        return  categoryDTO;
    }


    public  static  Category maptoCategory(CategoryDTO categoryDTO, Category category )
    {
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getCategoryName());


        return category;
    }


}
