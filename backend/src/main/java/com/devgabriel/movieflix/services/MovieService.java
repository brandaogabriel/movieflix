package com.devgabriel.movieflix.services;

import com.devgabriel.movieflix.dtos.MovieDTO;
import com.devgabriel.movieflix.entities.Movie;
import com.devgabriel.movieflix.repositories.MovieRepository;
import com.devgabriel.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MovieService {

  @Autowired
  private MovieRepository repository;

  @Transactional(readOnly = true)
  public MovieDTO findById(Long id) {
    Optional<Movie> obj = repository.findById(id);
    Movie movie = obj.orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + id));
    return new MovieDTO(movie, movie.getReviews());
  }

}
