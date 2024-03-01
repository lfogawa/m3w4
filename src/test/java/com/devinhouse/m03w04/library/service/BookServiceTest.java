package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.dtos.BookRequest;
import com.devinhouse.m03w04.library.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookService bookService;

    @Mock
    private PersonService personService;

    @Mock
    private BookRepository bookRepository;

    private BookRequest inputBookRequest;

    @Test
    void createBookWithSuccess() {
        when(personService.findByEmail(null))
                .thenReturn(new Person("1L", "testuser@example.com", "Test User"));

        when(bookRepository.findByTitleAndRegisteredBy(any(), any()))
                .thenReturn(Optional.empty());

        Person registeredUser = new Person("1L", "testuser@example.com", "Test User");

        BookRequest request = new BookRequest(null, "Test Book", 2022, registeredUser, Collections.emptyList());

        when(bookRepository.save(any()))
                .thenReturn(new Book("Test Book", 2022, registeredUser, Collections.emptyList()));

        ResponseEntity<BookRequest> responseEntity = bookService.create(request, Mockito.mock(UserDetails.class));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        BookRequest response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("Test Book", response.title());
        assertEquals(2022, response.year());
        assertEquals("testuser@example.com", response.registeredBy().getUsername());
    }

    @Test
    void createBookReturnsIllegalArgumentException() {
        PersonService personService = Mockito.mock(PersonService.class);
        when(personService.findByEmail(any()))
                .thenReturn(new Person("Test User", "testuser@example.com", "Test User"));

        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        RatingService ratingService = Mockito.mock(RatingService.class);

        BookService bookService = new BookService(bookRepository, personService, ratingService);

        Person registeredUser = new Person("Test User", "testuser@example.com", "Test User");
        BookRequest request = new BookRequest(null, null, null, registeredUser, Collections.emptyList());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bookService.create(request, Mockito.mock(UserDetails.class));
        });

        Assertions.assertTrue(exception.getMessage().contains("Title or year must not be null or empty"));
    }
}