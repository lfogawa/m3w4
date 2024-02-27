package com.devinhouse.m03w04.library.model.dtos.operations.create;

import jakarta.validation.constraints.NotBlank;

public record CreatePersonForm(@NotBlank String name,
                               @NotBlank String email,
                               @NotBlank String password) {
}
