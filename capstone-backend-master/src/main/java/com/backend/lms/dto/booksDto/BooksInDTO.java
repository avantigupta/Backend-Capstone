package com.backend.lms.dto.booksDto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooksInDTO {

    private String title;
    private String author;

    private Long categoryId;

    private Integer quantity;

}
