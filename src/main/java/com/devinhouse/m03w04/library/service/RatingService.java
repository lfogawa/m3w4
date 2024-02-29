package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.Rating;
import com.devinhouse.m03w04.library.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;


    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating addRating(Book book, Person person, Double rating) {
        Optional<Rating> existingRating = ratingRepository.findByBookAndPerson(book, person);

        if (existingRating.isPresent()) {
            existingRating.get().setRating(rating);
            return ratingRepository.save(existingRating.get());
        } else {
            Rating newRating = new Rating();
            newRating.setBook(book);
            newRating.setPerson(person);
            newRating.setRating(rating);
            return ratingRepository.save(newRating);
        }
    }
}