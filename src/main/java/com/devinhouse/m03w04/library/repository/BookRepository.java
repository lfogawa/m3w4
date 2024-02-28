package com.devinhouse.m03w04.library.repository;

import com.devinhouse.m03w04.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findById(Integer bookId);


}
