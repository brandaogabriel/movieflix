package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.GenreDTO;
import com.devgabriel.movieflix.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/genres")
public class GenreResource {

  @Autowired
  private GenreService service;

  @GetMapping
  public ResponseEntity<List<GenreDTO>> findAllGenres() {
    return ResponseEntity.ok().body(service.findAllGenres());
  }
}
