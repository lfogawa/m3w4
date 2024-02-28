package com.devinhouse.m03w04.library.model.dtos;

import com.devinhouse.m03w04.library.model.Person;
import jakarta.validation.constraints.NotBlank;

public record PersonRequest(@NotBlank String guid, @NotBlank String name, @NotBlank String email) {
    public PersonRequest(Person person){
        this(person.getGuid(), person.getName(), person.getEmail());
    }

}
