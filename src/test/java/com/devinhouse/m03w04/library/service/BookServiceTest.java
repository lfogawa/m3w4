package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.dtos.BookRequest;
import com.devinhouse.m03w04.library.model.dtos.BookResponse;
import com.devinhouse.m03w04.library.model.Rating;
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
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    @Test
    void testGetByIdSuccess() {
        Book book = new Book(1, "Sample Book", 2022, new Person("Author", "author@example.com", "Author"));

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        PersonService personService = mock(PersonService.class);
        RatingService ratingService = mock(RatingService.class);

        BookService bookService = new BookService(bookRepository, personService, ratingService);

        ResponseEntity<Book> responseEntity = bookService.getById(1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Sample Book", responseEntity.getBody().getTitle());
        assertEquals(2022, responseEntity.getBody().getYear());
        assertEquals("Author", responseEntity.getBody().getRegisteredBy().getName());
    }

    @Test
    void testGetByIdException() {
        when(bookRepository.findById(2)).thenReturn(Optional.empty());

        PersonService personService = mock(PersonService.class);
        RatingService ratingService = mock(RatingService.class);

        BookService bookService = new BookService(bookRepository, personService, ratingService);

        ResponseEntity<Book> errorResponseEntity = bookService.getById(2);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponseEntity.getStatusCode());
        assertNull(errorResponseEntity.getBody());
    }

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

    @Test
    void getAllBooksWithAverageRatingWithSuccess() {
        Book book1 = new Book("Book 1", 2022, new Person("Author 1", "author1@example.com", "Author 1"), Arrays.asList(new Rating(4.0), new Rating(5.0)));
        Book book2 = new Book("Book 2", 2023, new Person("Author 2", "author2@example.com", "Author 2"), Arrays.asList(new Rating(3.0), new Rating(4.0)));

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        ResponseEntity<List<BookResponse>> responseEntity = bookService.getAllBooksWithAverageRating();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<BookResponse> bookResponses = responseEntity.getBody();
        assertNotNull(bookResponses);
        assertEquals(2, bookResponses.size());

        assertEquals("Book 1", bookResponses.get(0).title());
        assertEquals(2022, bookResponses.get(0).year());
        assertEquals("Author 1", bookResponses.get(0).registeredBy().getName());
        assertEquals(4.5, bookResponses.get(0).averageRating(), 0.01);
        assertEquals(1, bookResponses.get(0).ratingCounts().get(4.0));
        assertEquals(1, bookResponses.get(0).ratingCounts().get(5.0));

        assertEquals("Book 2", bookResponses.get(1).title());
        assertEquals(2023, bookResponses.get(1).year());
        assertEquals("Author 2", bookResponses.get(1).registeredBy().getName());
        assertEquals(3.5, bookResponses.get(1).averageRating(), 0.01);
        assertEquals(1, bookResponses.get(1).ratingCounts().get(3.0));
        assertEquals(1, bookResponses.get(1).ratingCounts().get(4.0));
    }

    @Test
    void testGetAllBooksWithAverageRatingReturnsException() {
        when(bookRepository.findAll()).thenThrow(new RuntimeException("Simulating an exception"));

        ResponseEntity<List<BookResponse>> responseEntity = bookService.getAllBooksWithAverageRating();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}