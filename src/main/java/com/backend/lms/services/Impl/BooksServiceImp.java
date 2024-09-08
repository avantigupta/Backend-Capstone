package com.backend.lms.services.Impl;

import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.dto.booksDto.BooksOutDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Category;
import com.backend.lms.entities.Issuance;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.BooksMapper;
import com.backend.lms.repositories.BooksRepository;
import com.backend.lms.repositories.CategoryRepository;
import com.backend.lms.repositories.IssuanceRepository;
import com.backend.lms.services.IBooksService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BooksServiceImp implements IBooksService {

    @Autowired
    private final BooksRepository booksRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final IssuanceRepository issuanceRepository;

    @Override
    public BooksOutDTO createBook(BooksInDTO booksDTO) {
        Books book=BooksMapper.mapToBooks(booksDTO,categoryRepository);

         Books savedBooks=booksRepository.save(book);
         BooksOutDTO booksOutDTO=BooksMapper.mapToBooksOutDTO(savedBooks,categoryRepository);
         return booksOutDTO;
    }

    @Override
    public String deleteBook(Long id) {
        if (!booksRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with ID: " + id);
        }
        booksRepository.deleteById(id);
        return "Book deleted successfully with ID: " + id;
    }

    @Override
    public String updateBook(Long id, BooksInDTO booksDTO) {
        Books existingBook = booksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        existingBook.setTitle(booksDTO.getTitle());
        existingBook.setAuthor(booksDTO.getAuthor());
        existingBook.setQuantity(booksDTO.getQuantity());

        Category category = categoryRepository.findById(booksDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + booksDTO.getCategoryId()));
        existingBook.setCategory(category);
        booksRepository.save(existingBook);

        return "Book updated successfully with ID: " + id;
    }

    @Override
    public List<Issuance> getIssuancesByBookId(Long bookId) {
        return issuanceRepository.findByBookId(bookId);
    }

//    @Override
//    public List<BooksOutDTO> getBooks() {
//        List<Books> books = booksRepository.findAll();
//        List<BooksOutDTO> booksDTOList = new ArrayList<>();
//
//        books.forEach(book -> {
//            booksDTOList.add(BooksMapper.mapToBooksOutDTO(book, categoryRepository));
//        });
//
//        return booksDTOList;
//    }

    @Override
    public Page<Books> getBooks(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        if (search != null && !search.isEmpty()) {
            return booksRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search, pageable);
        } else {
            return booksRepository.findAll(pageable);
        }
    }


    @Override
    public Long getBookCount() {
        return booksRepository.count();
    }
}
