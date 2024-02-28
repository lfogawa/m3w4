package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.dtos.PersonRequest;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.dtos.operations.create.CreatePersonForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.devinhouse.m03w04.library.repository.PersonRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService implements UserDetailsService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder){
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.personRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User by e-mail not found: %s", username)));
    }

    public Person findByEmail(String email) throws UsernameNotFoundException{
        return this.personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User by e-mail not found: %s", email)));
    }

    public Person findByGuid(String guid) throws UsernameNotFoundException{
        return this.personRepository.findByGuid(guid)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User by GUID not found: %s", guid)));
    }

    public PersonRequest getByGuid(String guid) throws UsernameNotFoundException {
        return this.personRepository.findByGuid(guid).map(PersonRequest::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User by GUID not found: %s", guid)));
    }


    @Transactional
    public ResponseEntity<String> create(CreatePersonForm form) {
        try {
            if (personRepository.existsByEmail(form.email())) {
                throw new Exception("User with this email already exists");
            }

            String passwordEncoded = this.passwordEncoder.encode(form.password());
            Person person = this.personRepository.save(new Person(form, passwordEncoded));
            return ResponseEntity.ok("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
