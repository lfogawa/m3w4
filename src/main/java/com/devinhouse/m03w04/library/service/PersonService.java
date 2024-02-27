package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.dtos.PersonRequest;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.dtos.operations.create.CreatePersonForm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.devinhouse.m03w04.library.repository.PersonRepository;
import org.springframework.transaction.annotation.Transactional;

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

    public Person findById(String guid) throws UsernameNotFoundException{
        return this.personRepository.findByGuid(guid)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User by GUID not found: %s", guid)));
    }

    public PersonRequest getByGuid(String guid) throws UsernameNotFoundException {
        return this.personRepository.findByGuid(guid).map(PersonRequest::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User by GUID not found: %s", guid)));
    }


    @Transactional
    public PersonRequest create(CreatePersonForm form){
        String password = this.passwordEncoder.encode(form.password());
        Person person = new Person(form, password);
        this.personRepository.save(person);
        return new PersonRequest(person);
    }
}
