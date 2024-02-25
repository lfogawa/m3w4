package com.devinhouse.m03w04.library.model;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Entity
@Table(name = "RATING")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Range(min = 1, max = 5)
    @Column(nullable = false)
    private Integer score;

    @ManyToOne
    private Person person;

    @ManyToOne
    private Book book;


}
