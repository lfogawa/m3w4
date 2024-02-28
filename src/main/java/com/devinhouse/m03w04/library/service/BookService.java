package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.dtos.BookRequest;
import com.devinhouse.m03w04.library.repository.BookRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final PersonService personService;

    public BookService(BookRepository bookRepository, PersonService personService){
        this.bookRepository = bookRepository;
        this.personService = personService;
    }

    public BookRequest getById(Integer bookId) throws Exception{
        return this.bookRepository.findById(bookId).map(BookRequest::new)
                .orElseThrow(() -> new Exception(String.format("Book by id not found: %s", bookId)));
    }

    @Transactional
    public BookRequest create(BookRequest body, UserDetails userInSession) throws Exception{
        Person person = this.personService.findByEmail(userInSession.getUsername());
        Book book = new Book(null, body.title(), body.year(), person);
        book = this.bookRepository.save(book);
        return new BookRequest(book);
    }
}
