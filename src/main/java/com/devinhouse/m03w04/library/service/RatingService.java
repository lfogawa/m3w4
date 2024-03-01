package com.devinhouse.m03w04.library.service;

import com.devinhouse.m03w04.library.model.Book;
import com.devinhouse.m03w04.library.model.Person;
import com.devinhouse.m03w04.library.model.Rating;
import com.devinhouse.m03w04.library.repository.RatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;


    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public ResponseEntity<Rating> addRating(Book book, Person person, Double rating) {
        Optional<Rating> existingRating = ratingRepository.findByBookAndPerson(book, person);

        try {
            Rating addedRating;
            if (existingRating.isPresent()) {
                existingRating.get().setRating(rating);
                addedRating = ratingRepository.save(existingRating.get());
            } else {
                Rating newRating = new Rating();
                newRating.setBook(book);
                newRating.setPerson(person);
                newRating.setRating(rating);
                addedRating = ratingRepository.save(newRating);
            }

            return ResponseEntity.ok(addedRating);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error when add rating.", e);
        }
    }
}