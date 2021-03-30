package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.MovieDTO;
import com.devgabriel.movieflix.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/movies")
public class MovieResource {

  @Autowired
  private MovieService service;

  @GetMapping(value = "/{id}")
  public ResponseEntity<MovieDTO> findById(@PathVariable Long id) {
    MovieDTO movie = service.findById(id);
    return ResponseEntity.ok().body(movie);
  }
}
