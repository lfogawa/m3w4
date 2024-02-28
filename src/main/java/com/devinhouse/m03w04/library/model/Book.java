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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "registered_by_guid", referencedColumnName = "guid", nullable = false)
    @JoinColumn(name = "registered_by_email", referencedColumnName = "email", nullable = false)
    private Person registeredBy;

    @Column(nullable = false)
    private Integer year;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private List<Rating> ratings;

    public Double calculateAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }

        double sum = ratings.stream()
                .mapToDouble(Rating::getRating)
                .sum();

        return sum / ratings.size();
    }

    public Book() {
    }

    public Book(Integer bookId, String title, Integer year, Person person) {
        this.bookId = bookId;
        this.title = title;
        this.year = year;
        this.registeredBy = person;
    }

    public Book(String title, Integer year, Person person, List<Rating> ratings) {
        this.title = title;
        this.year = year;
        this.registeredBy = person;
        this.ratings = ratings;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Person getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(Person registeredBy) {
        this.registeredBy = registeredBy;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
