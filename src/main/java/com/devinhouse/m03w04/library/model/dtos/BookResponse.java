package com.devinhouse.m03w04.library.model.dtos;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.Rating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record BookResponse (Integer bookId, @NotBlank String title, @NotNull Integer year, @NotNull Person registeredBy, Double averageRating, Map<Double, Integer> ratingCounts){
    public BookResponse(Book book) {
        this(book.getBookId(), book.getTitle(), book.getYear(), book.getRegisteredBy(), book.calculateAverageRating(), calculateRatingCounts(book.getRatings()));
    }

    public static Map<Double, Integer> calculateRatingCounts(List<Rating> ratings) {
        Map<Double, Integer> ratingCounts = new HashMap<>();

        for (Rating rating : ratings) {
            double ratingValue = rating.getRating();

            ratingValue = Math.round(ratingValue * 10.0) / 10.0;

            ratingCounts.put(ratingValue, ratingCounts.getOrDefault(ratingValue, 0) + 1);
        }

        return ratingCounts;
    }
}
