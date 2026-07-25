package com.devgabriel.movieflix.dtos;

import com.devgabriel.movieflix.entities.Movie;
import com.devgabriel.movieflix.entities.Review;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({
        "id",
        "title",
        "subTitle",
        "year",
        "imgUrl",
        "synopsis",
        "genreId",
        "reviews"
})
public class MovieDTO implements Serializable {

  @Schema(example = "1")
  private Long id;

  @Schema(example = "Miraculous: As aventuras de LadyBug")
  private String title;

  @Schema(example = "Prepara-se para a maior aventura!")
  private String subTitle;

  @Schema(example = "2015")
  private Integer year;

  @Schema(example = "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/aSy0zROlkSSJEcHj7UvdvPOaMpq.jpg")
  private String imgUrl;

  @Schema(example = "Ladybug é uma heroína que tem a missão de defender Paris de um vilão misterioso. Junto com o parceiro Cat Noir, eles devem conciliar o dia a dia com a vida de super-heróis.")
  private String synopsis;

  @Schema(example = "1")
  private Long genreId;

  @Schema
  private final List<ReviewDTO> reviews = new ArrayList<>();

  public MovieDTO() {
  }

  public MovieDTO(Movie entity) {
    id = entity.getId();
    title = entity.getTitle();
    subTitle = entity.getSubTitle();
    year = entity.getYear();
    imgUrl = entity.getImgUrl();
    synopsis = entity.getSynopsis();
    genreId = entity.getGenre().getId();
  }

  public MovieDTO(Movie entity, List<Review> reviews) {
    this(entity);
    reviews.forEach(review -> this.reviews.add(new ReviewDTO(review)));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubTitle() {
    return subTitle;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public String getSynopsis() {
    return synopsis;
  }

  public void setSynopsis(String synopsis) {
    this.synopsis = synopsis;
  }

  public Long getGenreId() {
    return genreId;
  }

  public void setGenreId(Long genreId) {
    this.genreId = genreId;
  }

  public List<ReviewDTO> getReviews() {
    return reviews;
  }
}
