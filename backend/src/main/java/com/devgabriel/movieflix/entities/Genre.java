package com.devgabriel.movieflix.entities;

import com.devgabriel.movieflix.common.LogFields;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "genre")
public class Genre extends LogFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "genre")
  private final List<Movie> movies = new ArrayList<>();

  public Genre() {
  }

  public Genre(Long id, String name) {
    this.id = id;
    this.name = name;
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

  public List<Movie> getMovies() {
    return movies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Genre genre = (Genre) o;
    return Objects.equals(id, genre.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
