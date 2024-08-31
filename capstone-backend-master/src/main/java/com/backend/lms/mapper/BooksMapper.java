package com.backend.lms.mapper;

import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.dto.booksDto.BooksOutDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Category;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.repositories.CategoryRepository;

public class BooksMapper {

    private BooksMapper() {
        // Private constructor to prevent instantiation
    }

    // Maps a Books entity to a BooksOutDTO
    public static BooksOutDTO mapToBooksOutDTO(Books books, CategoryRepository categoryRepository) {
        BooksOutDTO booksOutDTO = new BooksOutDTO();
        booksOutDTO.setId(books.getId());
        booksOutDTO.setTitle(books.getTitle());
        booksOutDTO.setAuthor(books.getAuthor());
        booksOutDTO.setQuantity(books.getQuantity());

        // Fetch the Category entity using categoryId from Books
        if (books.getCategoryId() != null) {
            Category category = categoryRepository.findById(books.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found for id: " + books.getCategoryId()));
            booksOutDTO.setCategory(CategoryMapper.mapToCategoryDTO(category));
        }

        return booksOutDTO;
    }

    // Maps a BooksInDTO to a Books entity
    public static Books mapToBooks(BooksInDTO booksInDTO, CategoryRepository categoryRepository) {
        Books book = new Books();
        book.setTitle(booksInDTO.getTitle());
        book.setAuthor(booksInDTO.getAuthor());
        book.setQuantity(booksInDTO.getQuantity());

        // Set categoryId from BooksInDTO to Books
        book.setCategoryId(booksInDTO.getCategoryId());

        return book;
    }
}
