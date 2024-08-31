package com.backend.lms.services.Impl;

import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.dto.booksDto.BooksOutDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.BooksMapper;
import com.backend.lms.repositories.BooksRepository;
import com.backend.lms.repositories.CategoryRepository;
import com.backend.lms.services.IBooksService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        existingBook.setCategoryId(booksDTO.getCategoryId());
        existingBook.setQuantity(booksDTO.getQuantity());

        booksRepository.save(existingBook);

        return "Book updated successfully with ID: " + id;
    }

    @Override
    public List<BooksOutDTO> getBooks() {
        List<Books> books = booksRepository.findAll();
        List<BooksOutDTO> booksDTOList = new ArrayList<>();

        books.forEach(book -> {
            booksDTOList.add(BooksMapper.mapToBooksOutDTO(book, categoryRepository));
        });

        return booksDTOList;
    }

    @Override
    public Long getBookCount() {
        return booksRepository.count();
    }
}
