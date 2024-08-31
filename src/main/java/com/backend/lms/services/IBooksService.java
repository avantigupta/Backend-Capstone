package com.backend.lms.services;

import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.dto.booksDto.BooksOutDTO;

import java.util.List;

public interface IBooksService {

    BooksOutDTO createBook(BooksInDTO booksInDTO);

    String deleteBook(Long id);

    String updateBook(Long id, BooksInDTO booksInDTO);

    List<BooksOutDTO> getBooks();

    Long getBookCount();

}
