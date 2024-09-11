package com.backend.lms.controllers;

import com.backend.lms.dto.ResponseDto.ResponseDTO;
import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Issuance;
import com.backend.lms.services.IBooksService;
import com.backend.lms.services.ICategoryService;
import com.backend.lms.services.IIssuanceService;
import com.backend.lms.services.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.backend.lms.constants.constants.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/books")
public class BooksController {

    @Autowired
    private IBooksService booksService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IIssuanceService issuanceService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> createBook(@RequestBody BooksInDTO booksDTO) {
        booksService.createBook(booksDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, BOOK_CREATE_MESSAGE));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateBook(@PathVariable Long id, @RequestBody BooksInDTO booksDTO) {
        booksService.updateBook(id, booksDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, BOOK_UPDATE_MESSAGE));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteBook(@PathVariable Long id) {
        booksService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, BOOK_DELETE_MESSAGE));
    }

    @GetMapping("/list")
    public Page<Books> getBooks(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "5") int size,
        @RequestParam(value = "search", required = false) String search
    ) {
    return booksService.getBooks(page, size, search);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getBookCount() {
        Long bookCount = booksService.getBookCount();
        return ResponseEntity.status(HttpStatus.OK).body(bookCount);
    }

    @GetMapping("/{id}/issuances")
    public ResponseEntity<List<Issuance>> getIssuancesByBookId(@PathVariable Long id) {
        List<Issuance> issuancesList = booksService.getIssuancesByBookId(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuancesList);
    }

    @GetMapping("/count/all")
    public ResponseEntity<Map<String, Object>> getAllCounts() {
        Map<String, Object> counts = new HashMap<>();

        Long bookCount = booksService.getBookCount();
        Long categoryCount = categoryService.getCategoryCount();
        Long userCount = usersService.getUserCount();
        Map<String, Long> issuanceCountByType = issuanceService.getCountByIssuanceType();
        Long activeUserCount = issuanceService.getIssuedCount();

        counts.put("bookCount", bookCount);
        counts.put("categoryCount", categoryCount);
        counts.put("userCount", userCount);
        counts.put("issuanceCountByType", issuanceCountByType);
        counts.put("activeUserCount", activeUserCount);

        return ResponseEntity.status(HttpStatus.OK).body(counts);
    }
}
