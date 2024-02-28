package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.dtos.BookRequest;
import com.devinhouse.m03w04.library.model.dtos.BookResponse;
import com.devinhouse.m03w04.library.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.devinhouse.m03w04.library.model.Rating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final PersonService personService;

    public BookService(BookRepository bookRepository, PersonService personService){
        this.bookRepository = bookRepository;
        this.personService = personService;
    }

    public BookResponse getById(Integer bookId) throws Exception{
        return this.bookRepository.findById(bookId).map(BookResponse::new)
                .orElseThrow(() -> new Exception(String.format("Book by id not found: %s", bookId)));
    }

    @Transactional
    public BookRequest create(BookRequest body, UserDetails userInSession) throws Exception {
        Person registeredBy = this.personService.findByEmail(userInSession.getUsername());

        List<Rating> ratings = (body.ratings() != null) ? body.ratings() : new ArrayList<>();

        Book book = new Book(body.title(), body.year(), registeredBy, ratings);

        book = this.bookRepository.save(book);

        return new BookRequest(book);
    }

    public List<BookResponse> getAllBooksWithAverageRating() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> new BookResponse(
                        book.getBookId(),
                        book.getTitle(),
                        book.getYear(),
                        book.getRegisteredBy(),
                        calculateAverageRating(book)
                ))
                .collect(Collectors.toList());
    }

    private Double calculateAverageRating(Book book) {
        List<Rating> ratings = book.getRatings();
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }

        ratings.size();

        double sum = ratings.stream()
                .mapToDouble(Rating::getRating)
                .sum();

        return sum / ratings.size();
    }
}
