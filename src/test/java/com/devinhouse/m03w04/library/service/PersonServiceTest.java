package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.dtos.operations.create.CreatePersonForm;
import com.devinhouse.m03w04.library.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @InjectMocks
    PersonService personService;

    @Mock
    PersonRepository personRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    private CreatePersonForm createPersonForm;

    @BeforeEach
    public void setUp() {
        String username = "postgres";
        String email = "postgres@gmail.com";
        String password = "postgres";
        createPersonForm = new CreatePersonForm(username, email, password);
    }

    @Test
    public void createPersonWithSuccess() {
        String rawPassword = createPersonForm.password();
        String encodedPassword = "cG9zdGdyZXM=";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        ResponseEntity<String> response = personService.create(createPersonForm);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Person created successfully", response.getBody());
    }

    @Test
    public void createPersonReturnsBadRequest() {
        CreatePersonForm personWithSameEmail = new CreatePersonForm("postgres", "postgres@gmail.com", "postgres");
        when(personRepository.existsByEmail(personWithSameEmail.email())).thenReturn(true);

        ResponseEntity<String> response = personService.create(personWithSameEmail);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Person with this email already exists", response.getBody());
    }

//    @Test
//    public void createPersonReturnsException(){
//        CreatePersonForm personWithoutEmail = new CreatePersonForm("postgres", "", "postgres");
//
//        ResponseEntity<String> response = personService.create(personWithoutEmail);
//        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        Assertions.assertEquals("Error creating person", response.getBody());
//    }
}