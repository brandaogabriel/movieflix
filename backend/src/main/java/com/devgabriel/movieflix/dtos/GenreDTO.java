package com.devgabriel.movieflix.dtos;

import com.devgabriel.movieflix.entities.Genre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenreDTO implements Serializable {

  private Long id;
  private String name;
  private final List<MovieDTO> movies = new ArrayList<>();

  public GenreDTO() {
  }

  public GenreDTO(Genre entity) {
    id = entity.getId();
    name = entity.getName();
    entity.getMovies().forEach(movie -> this.movies.add(new MovieDTO(movie)));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<MovieDTO> getMovies() {
    return movies;
  }
}
