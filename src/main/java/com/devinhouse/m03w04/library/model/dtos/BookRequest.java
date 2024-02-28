package com.devinhouse.m03w04.library.model.dtos;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookRequest(Integer bookId, @NotBlank String title, @NotNull Integer year, @NotNull Person owner){

    public BookRequest(Book book){
        this(book.getBookId(), book.getTitle(), book.getYear(), book.getOwner());
    }
}