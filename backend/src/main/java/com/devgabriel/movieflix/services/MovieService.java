package com.devgabriel.movieflix.services;

import com.devgabriel.movieflix.dtos.MovieDTO;
import com.devgabriel.movieflix.entities.Genre;
import com.devgabriel.movieflix.entities.Movie;
import com.devgabriel.movieflix.repositories.GenreRepository;
import com.devgabriel.movieflix.repositories.MovieRepository;
import com.devgabriel.movieflix.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger LOG = LoggerFactory.getLogger(MovieService.class);

  @Autowired
  private MovieRepository repository;

  @Autowired
  private GenreRepository genreRepository;

  @Transactional(readOnly = true)
  public List<MovieDTO> findAll() {
    LOG.info("method=findAll, msg=find all movies test api");
    List<Movie> movies = repository.findAll();
    return movies.stream()
            .map(MovieDTO::new)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Page<MovieDTO> findAllPaged(Long genreId, PageRequest pageRequest) {
    LOG.info("method=findAllPaged, msg=find all movies paged order by title");
    Genre genre = (genreId == 0) ? null : genreRepository.getOne(genreId);
    Page<Movie> movies = repository.find(genre, pageRequest);
    return movies.map(MovieDTO::new);
  }

  @Transactional(readOnly = true)
  public MovieDTO findById(Long id) {
    LOG.info("method=findById, msg=find movie by id {}", id);
    Optional<Movie> obj = repository.findById(id);
    Movie movie = obj.orElseThrow(() -> {
      LOG.error("method=findById, msg=movie with id {} not found", id);
      throw new ResourceNotFoundException("Movie not found: " + id);
    });
    return new MovieDTO(movie, movie.getReviews());
  }

}
