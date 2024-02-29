package com.devinhouse.m03w04.library.model.controller;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.dtos.BookRequest;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.dtos.BookResponse;
import com.devinhouse.m03w04.library.service.BookService;
import com.devinhouse.m03w04.library.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooksWithAverageRating();
        return ResponseEntity.ok(books);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookRequest> create(@AuthenticationPrincipal UserDetails userInSession,
                                              @Valid @RequestBody BookRequest body,
                                              UriComponentsBuilder uriComponentsBuilder) throws Exception {
        BookRequest response = this.bookService.create(body, userInSession);
        UriComponents uriComponents = uriComponentsBuilder.path("/book/{id}").buildAndExpand(response.bookId());
        return ResponseEntity.created(uriComponents.toUri()).body(response);
    }

    @PostMapping("/{bookId}/assessment")
    public ResponseEntity<?> addAssessment(
            @PathVariable Integer bookId,
            @RequestBody Map<String, Double> requestBody,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Double rating = requestBody.get("rating");

        try {
            bookService.addRatingToBook(bookId, rating, userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body("Avaliação adicionada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao adicionar avaliação: " + e.getMessage());
        }
    }
}
