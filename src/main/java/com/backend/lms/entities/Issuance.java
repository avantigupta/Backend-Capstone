package com.backend.lms.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Issuance")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Issuance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(nullable = false)
    private String status;

    @Column(name = "issuance_type", nullable = false)
    private String issuanceType;


}
