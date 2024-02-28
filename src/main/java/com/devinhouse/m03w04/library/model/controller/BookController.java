package com.devinhouse.m03w04.library.model.controller;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.dtos.BookRequest;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.service.BookService;
import com.devinhouse.m03w04.library.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRequest> get(@PathVariable("id") Integer bookId) throws Exception {
        BookRequest response = this.bookService.getById(bookId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookRequest> create(@AuthenticationPrincipal UserDetails userInSession,
                                              @Valid @RequestBody BookRequest body,
                                              UriComponentsBuilder uriComponentsBuilder) throws Exception{
        BookRequest response = this.bookService.create(body, userInSession);
        UriComponents uriComponents = uriComponentsBuilder.path("/book/{id}").buildAndExpand(response.bookId());
        return ResponseEntity.created(uriComponents.toUri()).body(response);
    }
}
