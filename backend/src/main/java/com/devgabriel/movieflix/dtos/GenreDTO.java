package com.devgabriel.movieflix.dtos;

import com.devgabriel.movieflix.entities.Genre;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;


public class GenreDTO implements Serializable {

  @Schema(example = "1")
  private Long id;

  @Schema(example = "Action")
  private String name;

  public GenreDTO() {
  }

  public GenreDTO(Genre entity) {
    id = entity.getId();
    name = entity.getName();
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

}
