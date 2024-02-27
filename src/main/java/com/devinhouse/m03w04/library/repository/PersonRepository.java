package com.devinhouse.m03w04.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.devinhouse.m03w04.library.model.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);

    Optional<Person> findByGuid(String guid);

}
