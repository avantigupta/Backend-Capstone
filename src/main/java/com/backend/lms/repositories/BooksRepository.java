package com.backend.lms.repositories;

import com.backend.lms.entities.Books;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

//    @Query("SELECT b FROM Books b where b.category.categoryName LIKE :key")
    Page<Books> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);

}
