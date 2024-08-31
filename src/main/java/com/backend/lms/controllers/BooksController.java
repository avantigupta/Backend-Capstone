package com.backend.lms.controllers;

import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.dto.booksDto.BooksOutDTO;
import com.backend.lms.services.IBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/books")
public class BooksController {

    @Autowired
    private IBooksService iBooksService;

    // Create Book API
    @CrossOrigin
    @PostMapping("/save")
    public ResponseEntity<BooksOutDTO> createBook(@RequestBody BooksInDTO booksDTO) {
        BooksOutDTO booksOutDTO = iBooksService.createBook(booksDTO);
        return ResponseEntity.status(HttpStatus.OK).body(booksOutDTO);
    }

    // Update Book API
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody BooksInDTO booksDTO) {
        String message = iBooksService.updateBook(id, booksDTO);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // Delete Book API
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        String message = iBooksService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // Get All Books API
    @GetMapping("/getBooks")
    public ResponseEntity<List<BooksOutDTO>> getBooks() {
        List<BooksOutDTO> booksDTOList = iBooksService.getBooks();
        return ResponseEntity.status(HttpStatus.OK).body(booksDTOList);
    }

    // Get Book Count API
    @GetMapping("/books-count")
    public ResponseEntity<Long> getBookCount() {
        Long bookCount = iBooksService.getBookCount();
        return ResponseEntity.status(HttpStatus.OK).body(bookCount);
    }
}
