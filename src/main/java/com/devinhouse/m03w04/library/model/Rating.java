package com.devinhouse.m03w04.library.model;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "RATING")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id",nullable = false)
    private Integer ratingId;

    @Range(min = 1, max = 5)
    @Column(nullable = false)
    private Double rating;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL)
    private Book book;

    public Rating() {
    }

    public Rating(Double rating) {
        this.rating = rating;
    }

    public Rating(Person person, Double rating) {
        this.person = person;
        this.rating = rating;
    }

    public Rating(Book book, Person person, Double rating) {
        this.rating = rating;
        this.person = person;
        this.book = book;
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
        book.getRatings().add(this);
    }
}
