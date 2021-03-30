package com.devgabriel.movieflix.dtos;

import com.devgabriel.movieflix.entities.Review;

import java.io.Serializable;

public class ReviewDTO implements Serializable {

  private Long id;
  private String text;
  private UserDTO user;

  public ReviewDTO() {
  }

  public ReviewDTO(Review entity) {
    id = entity.getId();
    text = entity.getText();
    user = new UserDTO(entity.getUser());
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

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }
}
