package com.devgabriel.movieflix.services;

import com.devgabriel.movieflix.dtos.MovieDTO;
import com.devgabriel.movieflix.entities.Movie;
import com.devgabriel.movieflix.repositories.MovieRepository;
import com.devgabriel.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

  @Autowired
  private MovieRepository repository;

  @Transactional(readOnly = true)
  public List<MovieDTO> findAll() {
    List<Movie> movies = repository.findAll();
    return movies.stream()
            .map(MovieDTO::new)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Page<MovieDTO> findAllPaged(PageRequest pageRequest) {
    Page<Movie> movies = repository.findAll(pageRequest);
    return movies.map(MovieDTO::new);
  }

  @Transactional(readOnly = true)
  public MovieDTO findById(Long id) {
    Optional<Movie> obj = repository.findById(id);
    Movie movie = obj.orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + id));
    return new MovieDTO(movie, movie.getReviews());
  }

}
