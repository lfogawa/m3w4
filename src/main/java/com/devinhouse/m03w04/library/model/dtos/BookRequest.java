package com.devinhouse.m03w04.library.model.dtos;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.Rating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record BookRequest(Integer bookId, @NotBlank String title, @NotNull Integer year, @NotNull Person registeredBy, List<Rating> ratings) {
}