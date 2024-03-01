package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.dtos.BookRequest;
import com.devinhouse.m03w04.library.model.dtos.BookResponse;
import com.devinhouse.m03w04.library.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.devinhouse.m03w04.library.model.Rating;

import java.util.*;
import java.util.stream.Collectors;

import static com.devinhouse.m03w04.library.model.dtos.BookResponse.calculateRatingCounts;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final PersonService personService;
    private final RatingService ratingService;

    public BookService(BookRepository bookRepository, PersonService personService, RatingService ratingService){
        this.bookRepository = bookRepository;
        this.personService = personService;
        this.ratingService = ratingService;
    }

    public ResponseEntity<Book> getById(Integer bookId) {
        try {
            Book book = this.bookRepository.findById(bookId)
                    .orElseThrow(() -> new Exception(String.format("Book by id not found: %s", bookId)));
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    public ResponseEntity<BookRequest> create(BookRequest body, UserDetails userInSession) {
        try {
            Person registeredBy = this.personService.findByEmail(userInSession.getUsername());
            Optional<Book> existingBook = this.bookRepository.findByTitleAndRegisteredBy(body.title(), registeredBy);

            if (existingBook.isPresent()) {
                updateExistingRating(existingBook, body.ratings());
                Book updatedBook = existingBook.get();
                return ResponseEntity.ok(new BookRequest(
                        updatedBook.getBookId(),
                        updatedBook.getTitle(),
                        updatedBook.getYear(),
                        updatedBook.getRegisteredBy(),
                        updatedBook.getRatings()
                ));
            } else {
                List<Rating> ratings = (body.ratings() != null) ? body.ratings() : Collections.emptyList();
                Book newBook = new Book(body.title(), body.year(), registeredBy, ratings);

                if (newBook.getTitle() == null || newBook.getTitle().isEmpty() || newBook.getYear() == null) {
                    throw new IllegalArgumentException("Title or year must not be null or empty");
                }

                newBook = this.bookRepository.save(newBook);

                return ResponseEntity.ok(new BookRequest(
                        newBook.getBookId(),
                        newBook.getTitle(),
                        newBook.getYear(),
                        newBook.getRegisteredBy(),
                        newBook.getRatings()
                ));
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating/updating the book.", e);
        }
    }



    private void updateExistingRating(Optional<Book> existingBook, List<Rating> newRatings) {
        existingBook.ifPresent(book -> {
            for (Rating newRating : newRatings) {
                book.getRatings().stream()
                        .filter(existingRating -> Objects.equals(existingRating.getPerson(), newRating.getPerson()))
                        .findFirst()
                        .ifPresent(existingRating -> existingRating.setRating(newRating.getRating()));
            }
            this.bookRepository.save(book);
        });
    }

    @Transactional
    public void addRatingToBook(Integer bookId, Double rating, UserDetails userDetails) throws Exception {
        Person person = personService.findByEmail(userDetails.getUsername());
        Book book = getById(bookId).getBody();

        if (person.equals(book.getRegisteredBy())) {
            throw new Exception("Usuário não pode avaliar seu próprio livro.");
        }

        if (rating < 1 || rating > 5) {
            throw new Exception("A nota de avaliação deve estar entre 1 e 5.");
        }

        ratingService.addRating(book, person, rating);
    }

    public ResponseEntity<List<BookResponse>> getAllBooksWithAverageRating() {
        try {
            List<Book> books = bookRepository.findAll();
            List<BookResponse> bookResponses = books.stream()
                    .map(book -> new BookResponse(
                            book.getBookId(),
                            book.getTitle(),
                            book.getYear(),
                            book.getRegisteredBy(),
                            calculateAverageRating(book),
                            calculateRatingCounts(book.getRatings())
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(bookResponses);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Double calculateAverageRating(Book book) {
        List<Rating> ratings = book.getRatings();
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        double sum = ratings.stream()
                .mapToDouble(Rating::getRating)
                .sum();

        return sum / ratings.size();
    }
}
