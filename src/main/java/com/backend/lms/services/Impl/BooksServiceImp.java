package com.backend.lms.services.Impl;

import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.dto.booksDto.BooksOutDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Category;
import com.backend.lms.entities.Issuance;
import com.backend.lms.exception.BookAlreadyExistsException;
import com.backend.lms.exception.MethodNotAllowedException;
import com.backend.lms.exception.ResourceConflictException;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.BooksMapper;
import com.backend.lms.repositories.BooksRepository;
import com.backend.lms.repositories.CategoryRepository;
import com.backend.lms.repositories.IssuanceRepository;
import com.backend.lms.services.IBooksService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BooksServiceImp implements IBooksService {

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IssuanceRepository issuanceRepository;

    @Override
    public BooksOutDTO createBook(BooksInDTO booksDTO) {
        try {
            Optional<Books> existingBook = booksRepository.findByTitleAndAuthor(
                    booksDTO.getTitle(),
                    booksDTO.getAuthor()
            );

            if (existingBook.isPresent()) {
                throw new BookAlreadyExistsException("A book with the same title and author already exists.");
            }

            Books book = BooksMapper.mapToBooks(booksDTO, categoryRepository);
            Books savedBooks = booksRepository.save(book);
            BooksOutDTO booksOutDTO = BooksMapper.mapToBooksOutDTO(savedBooks, categoryRepository);
            return booksOutDTO;
        } catch (DataIntegrityViolationException e) {
            throw new BookAlreadyExistsException("A book with the same title and author already exists.");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating the book.", e);
        }
    }

    @Transactional
    @Override
    public String deleteBook(Long id) {
        if (!booksRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with ID: " + id);
        }
        boolean activeIssuances = issuanceRepository.existsByBookIdAndStatus(id,"ISSUED");

        if (activeIssuances) {
            throw new MethodNotAllowedException("Book is currently issued and cannot be deleted.");
        }
        issuanceRepository.deleteByBookId(id);
        booksRepository.deleteById(id);
        return "Book deleted successfully with ID: " + id;
    }


    @Override
    public String updateBook(Long id , BooksInDTO booksInDTO){

    Optional<Books> optionalBooks = booksRepository.findById(id);

    if (optionalBooks.isPresent()) {
        Optional<Books> bookWithSameTitle = booksRepository.findByTitle(booksInDTO.getTitle());
        if (bookWithSameTitle.isPresent() && !bookWithSameTitle.get().getId().equals(id)) {
            throw new ResourceConflictException("Book with the same title already exists.");
        }
        Books existingBook = optionalBooks.get();
        existingBook.setTitle(booksInDTO.getTitle());
        existingBook.setAuthor(booksInDTO.getAuthor());
        Optional<Category> optionalCategory = categoryRepository.findById(booksInDTO.getCategoryId());
        Category category = optionalCategory.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + booksInDTO.getCategoryId()));
        existingBook.setCategory(category);
        existingBook.setQuantity(booksInDTO.getQuantity());
        booksRepository.save(existingBook);
        return "Book updated successfully";
    } else {
        throw new ResourceNotFoundException("Book not found with ID: " + id);
    }
    }

    @Override
    public List<Issuance> getIssuancesByBookId(Long bookId) {
        return issuanceRepository.findByBookId(bookId);
    }

    @Override
    public Page<Books> getBooks(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
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
