package com.backend.lms.mapper;

import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.dto.booksDto.BooksOutDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Category;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.repositories.CategoryRepository;

public class BooksMapper {

    private BooksMapper() {
    }

    public static BooksOutDTO mapToBooksOutDTO(Books books, CategoryRepository categoryRepository) {
        BooksOutDTO booksOutDTO = new BooksOutDTO();
        booksOutDTO.setId(books.getId());
        booksOutDTO.setTitle(books.getTitle());
        booksOutDTO.setAuthor(books.getAuthor());
        booksOutDTO.setQuantity(books.getQuantity());

        if (books.getCategory() != null) {
           booksOutDTO.setCategory(CategoryMapper.mapToCategoryDTO(books.getCategory()));
        }

        return booksOutDTO;
    }
    public static Books mapToBooks(BooksInDTO booksInDTO, CategoryRepository categoryRepository) {
        Books book = new Books();
        book.setTitle(booksInDTO.getTitle());
        book.setAuthor(booksInDTO.getAuthor());
        book.setQuantity(booksInDTO.getQuantity());

        Category category = categoryRepository.findById(booksInDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + booksInDTO.getCategoryId()));
        book.setCategory(category);
        return book;
    }
}
