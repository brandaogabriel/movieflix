package com.devgabriel.movieflix.dtos;

import com.devgabriel.movieflix.entities.Review;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class ReviewDTO implements Serializable {

  @ApiModelProperty(position = 1, example = "1")
  private Long id;

  @ApiModelProperty(position = 2, example = "Nice movie, adorable!")
  @NotBlank(message = "Você deve informar um texto")
  @Size(min = 4, message = "O texto deve conter pelo menos quatro caracteres")
  private String text;

  @ApiModelProperty(position = 3)
  private UserDTO user;

  @ApiModelProperty(position = 4, example = "1")
  private Long movieId;

  public ReviewDTO() {
  }

  public ReviewDTO(Review entity) {
    id = entity.getId();
    text = entity.getText();
    movieId = entity.getMovie().getId();
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

  public Long getMovieId() {
    return movieId;
  }

  public void setMovieId(Long movieId) {
    this.movieId = movieId;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }
}
