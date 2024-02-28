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
    @JoinColumn(name = "owner_guid", referencedColumnName = "guid", nullable = false)
    @JoinColumn(name = "owner_email", referencedColumnName = "email", nullable = false)
    private Person owner;

    @Column(nullable = false)
    private Integer year;

    @OneToMany
    private List<Rating> ratings;

    public Book() {
    }

    public Book(Integer bookId, String title, Integer year, Person person) {
        this.bookId = bookId;
        this.title = title;
        this.year = year;
        this.owner = person;
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

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
