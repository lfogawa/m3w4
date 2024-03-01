package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.Rating;
import com.devinhouse.m03w04.library.model.dtos.BookRequest;
import com.devinhouse.m03w04.library.repository.BookRepository;
import com.devinhouse.m03w04.library.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {
    @InjectMocks
    RatingService ratingService;

    @Mock
    RatingRepository ratingRepository;

    @Mock
    private BookService bookService;

    @Mock
    private PersonService personService;

    @Mock
    private BookRepository bookRepository;

    @Test
    void addRatingWhenRatingDoesExist() {
        PersonService personService = Mockito.mock(PersonService.class);
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        RatingRepository ratingRepository = Mockito.mock(RatingRepository.class);

        Person registeredUser = new Person("1L", "testuser@example.com", "Test User");
        Book book = new Book(null, "Test Book", 2022, registeredUser);

        Rating existingRating = new Rating(book, registeredUser, 3.0);
        when(ratingRepository.findByBookAndPerson(any(Book.class), any()))
                .thenReturn(Optional.of(existingRating));

        when(ratingRepository.save(any(Rating.class)))
                .thenAnswer(invocation -> {
                    Rating updatedRating = invocation.getArgument(0);
                    existingRating.setRating(updatedRating.getRating());
                    return existingRating;
                });

        RatingService ratingService = new RatingService(ratingRepository);
        Rating addedRating = ratingService.addRating(book, registeredUser, 4.5).getBody();

        verify(ratingRepository, times(1)).findByBookAndPerson(any(), any());
        verify(ratingRepository, times(1)).save(any(Rating.class));

        assertEquals(4.5, addedRating.getRating());
        assertEquals(book, addedRating.getBook());
        assertEquals(registeredUser, addedRating.getPerson());
        assertEquals(existingRating.getRating(), addedRating.getRating());
    }


    @Test
    void addRatingWhenRatingDoesNotExist() {
        PersonService personService = Mockito.mock(PersonService.class);
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        RatingRepository ratingRepository = Mockito.mock(RatingRepository.class);

        Person registeredUser = new Person("1L", "testuser@example.com", "Test User");
        Book book = new Book(null, "Test Book", 2022, registeredUser);

        when(ratingRepository.findByBookAndPerson(any(Book.class), any()))
                .thenReturn(Optional.empty());

        when(ratingRepository.save(any(Rating.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RatingService ratingService = new RatingService(ratingRepository);
        Rating addedRating = ratingService.addRating(book, registeredUser, 4.5).getBody();

        verify(ratingRepository, times(1)).findByBookAndPerson(any(), any());
        verify(ratingRepository, times(1)).save(any(Rating.class));

        assertEquals(4.5, addedRating.getRating());
        assertEquals(book, addedRating.getBook());
        assertEquals(registeredUser, addedRating.getPerson());
    }

    @Test
    void addRatingReturnsException() {
        RatingRepository ratingRepository = Mockito.mock(RatingRepository.class);
        when(ratingRepository.findByBookAndPerson(any(Book.class), any()))
                .thenReturn(Optional.empty());
        when(ratingRepository.save(any(Rating.class)))
                .thenThrow(new RuntimeException("Simulated exception on save")); // Simula uma exceção ao salvar

        Book book = new Book(null, "Test Book", 2022, new Person("1L", "testuser@example.com", "Test User"));
        Person person = new Person("Test User", "testuser@example.com", "Test User");
        RatingService ratingService = new RatingService(ratingRepository);

        double ratingValue = 4.5;
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ratingService.addRating(book, person, ratingValue));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error when add rating.", exception.getReason());
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Simulated exception on save", exception.getCause().getMessage());
    }
}
