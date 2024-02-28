package com.devinhouse.m03w04.library.model.controller;

import com.devinhouse.m03w04.library.model.dtos.PersonRequest;
import com.devinhouse.m03w04.library.model.dtos.operations.create.CreatePersonForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.devinhouse.m03w04.library.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonRequest> get(@PathVariable("id") String guid) throws UsernameNotFoundException {
        PersonRequest response = this.personService.getByGuid(guid);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreatePersonForm createPersonForm) {
        try {
            ResponseEntity<String> response = personService.create(createPersonForm);
            if (response.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}