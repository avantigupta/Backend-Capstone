package com.backend.lms.repositories;

import com.backend.lms.entities.Issuance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface IssuanceRepository extends JpaRepository<Issuance, Long> {

    @Query("SELECT COUNT(i) FROM Issuance i WHERE i.issuanceType = :issuanceType")
    Long countByIssuanceType(@Param("issuanceType") String issuanceType);

    Optional<Issuance> findByUserIdAndBookId(Long userId, Long bookId);

    @Query("SELECT i FROM Issuance i WHERE i.user.id = :userId")
    List<Issuance> findByUserId(@Param("userId") Long userId);

    List<Issuance> findByBookId(Long bookId);

    Long countByStatus(String status);

    @Query("SELECT i FROM Issuance i " +
            "JOIN i.user u " +
            "JOIN i.book b " +
            "WHERE (:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Issuance> findByBookTitleOrUserName(@Param("search") String search, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM Issuance i WHERE i.book.id = :bookId AND i.status = :status")
    boolean existsByBookIdAndStatus(@Param("bookId") Long bookId, @Param("status") String status);

    void deleteByBookId(Long bookId);
    void deleteByUserId(Long userId);

    @Query("SELECT i FROM Issuance i WHERE i.returnedAt BETWEEN :start AND :end AND i.status = :status")
    List<Issuance> findAllByReturnedAtBetweenAndStatus(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") String status);

    boolean existsByUserIdAndStatus(Long userId, String status);

    List<Issuance> findByUserIdAndStatus(Long userId, String status);

}
