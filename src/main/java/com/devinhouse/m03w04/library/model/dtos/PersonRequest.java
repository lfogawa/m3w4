package com.devinhouse.m03w04.library.model.dtos;

import com.devinhouse.m03w04.library.model.Person;

public record PersonRequest(String guid, String name, String email) {
    public PersonRequest(Person person){
        this(person.getGuid(), person.getName(), person.getEmail());
    }
}
