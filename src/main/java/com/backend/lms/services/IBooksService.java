package com.backend.lms.services;

import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.dto.booksDto.BooksOutDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Issuance;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBooksService {

    BooksOutDTO createBook(BooksInDTO booksInDTO);

    String deleteBook(Long id);

    String updateBook(Long id, BooksInDTO booksInDTO);

    Page<Books> getBooks(int page, int size, String search);

    Long getBookCount();

    List<Issuance> getIssuancesByBookId(Long bookId);

}
