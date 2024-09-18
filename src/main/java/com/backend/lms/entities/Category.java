package com.backend.lms.entities;

import jakarta.persistence.*;

import lombok.*;
@Entity
@Table(name = "Category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categoryName;

}
