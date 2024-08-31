package com.backend.lms.dto.booksDto;


import com.backend.lms.dto.categoryDto.CategoryDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooksOutDTO {

    private Long id;
    private String title;
    private String author;
    private Integer quantity;
   private CategoryDTO category;
}
