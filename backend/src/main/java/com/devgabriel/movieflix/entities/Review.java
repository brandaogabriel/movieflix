package com.devgabriel.movieflix.entities;

import com.devgabriel.movieflix.common.LogFields;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "review")
public class Review extends LogFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String text;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "movie_id")
  private Movie movie;

  public Review() {
  }

  public Review(Long id, String text, User user, Movie movie) {
    this.id = id;
    this.text = text;
    this.user = user;
    this.movie = movie;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Movie getMovie() {
    return movie;
  }

  public void setMovie(Movie movie) {
    this.movie = movie;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Review review = (Review) o;
    return Objects.equals(id, review.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
