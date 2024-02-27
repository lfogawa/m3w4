package com.devinhouse.m03w04.library.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "BOOK")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id",nullable = false)
    private Integer bookId;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(nullable = false)
    private Integer year;

    @OneToMany
    private List<Rating> ratings;
}
