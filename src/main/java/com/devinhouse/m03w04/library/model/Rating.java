package com.devinhouse.m03w04.library.model;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Entity
@Table(name = "RATING")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id",nullable = false)
    private Integer ratingId;

    @Range(min = 1, max = 5)
    @Column(nullable = false)
    private Integer score;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    private Book book;


}
