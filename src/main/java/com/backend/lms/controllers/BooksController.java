package com.backend.lms.controllers;

import com.backend.lms.dto.ResponseDto.ResponseDTO;
import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.dto.booksDto.BooksOutDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Issuance;
import com.backend.lms.services.IBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.backend.lms.constants.constants.*;

@RestController
@RequestMapping(value = "api/books")
public class BooksController {

    @Autowired
    private IBooksService iBooksService;

    @CrossOrigin
    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> createBook(@RequestBody BooksInDTO booksDTO) {
        iBooksService.createBook(booksDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, CREATE_MESSAGE));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateBook(@PathVariable Long id, @RequestBody BooksInDTO booksDTO) {
        iBooksService.updateBook(id, booksDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, UPDATE_MESSAGE));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteBook(@PathVariable Long id) {
        iBooksService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, DELETE_MESSAGE));
    }

@GetMapping("/list")
public Page<Books> getBooks(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "5") int size,
        @RequestParam(value = "search", required = false) String search
) {
    return iBooksService.getBooks(page, size, search);
}

    @GetMapping("/count")
    public ResponseEntity<Long> getBookCount() {
        Long bookCount = iBooksService.getBookCount();
        return ResponseEntity.status(HttpStatus.OK).body(bookCount);
    }

    @CrossOrigin
    @GetMapping("/{id}/issuances")
    public ResponseEntity<List<Issuance>> getIssuancesByBookId(@PathVariable Long id) {
        List<Issuance> issuancesList = iBooksService.getIssuancesByBookId(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuancesList);
    }


}
