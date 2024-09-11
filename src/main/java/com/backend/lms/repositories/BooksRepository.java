package com.backend.lms.repositories;

import com.backend.lms.entities.Books;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

    Page<Books> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);

    Optional<Books> findByTitleAndAuthor(String title, String author);

    Optional<Books> findByTitle(String title);

    List<Books> findByCategoryId(Long categoryId);

}
