package com.devinhouse.m03w04.library.model.dtos;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookResponse (Integer bookId, @NotBlank String title, @NotNull Integer year, @NotNull Person registeredBy, Double averageRating){
    public BookResponse(Book book) {
        this(book.getBookId(), book.getTitle(), book.getYear(), book.getRegisteredBy(), book.calculateAverageRating());
    }
}