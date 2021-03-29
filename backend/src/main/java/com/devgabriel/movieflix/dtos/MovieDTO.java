package com.devgabriel.movieflix.dtos;

import com.devgabriel.movieflix.entities.Movie;

import java.io.Serializable;

public class MovieDTO implements Serializable {

  private Long id;
  private String title;
  private String subTitle;
  private Integer year;
  private String imgUrl;
  private String synopsis;
  private GenreDTO genreDTO;

  public MovieDTO() {
  }

  public MovieDTO(Movie entity) {
    id = entity.getId();
    title = entity.getTitle();
    subTitle = entity.getSubTitle();
    year = entity.getYear();
    imgUrl = entity.getImgUrl();
    synopsis = entity.getSynopsis();
    genreDTO = new GenreDTO(entity.getGenre());
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

  public GenreDTO getGenreDTO() {
    return genreDTO;
  }

  public void setGenreDTO(GenreDTO genreDTO) {
    this.genreDTO = genreDTO;
  }

}
