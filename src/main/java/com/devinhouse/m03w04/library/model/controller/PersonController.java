package com.devinhouse.m03w04.library.model.controller;

import com.devinhouse.m03w04.library.model.dtos.PersonRequest;
import com.devinhouse.m03w04.library.model.dtos.operations.create.CreatePersonForm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.devinhouse.m03w04.library.service.PersonService;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<PersonRequest> create(@RequestBody @Valid CreatePersonForm form, UriComponentsBuilder uriComponentsBuilder) {
        PersonRequest response = this.personService.create(form);
        return ResponseEntity.created(uriComponentsBuilder.path("/person/{id}").buildAndExpand(response.guid()).toUri()).body(response);
    }

}