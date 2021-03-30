package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.MovieDTO;
import com.devgabriel.movieflix.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/movies")
public class MovieResource {

  @Autowired
  private MovieService service;

  @GetMapping(value = "/api/test")
  public ResponseEntity<List<MovieDTO>> findAll() {
    List<MovieDTO> movies = service.findAll();
    return ResponseEntity.ok().body(movies);
  }

  @GetMapping
  public ResponseEntity<Page<MovieDTO>> findAllPaged(
          @RequestParam(value = "page", defaultValue = "0") Integer page,
          @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
          @RequestParam(value = "direction", defaultValue = "ASC") String direction,
          @RequestParam(value = "orderBy", defaultValue = "title") String orderBy
  ) {

    PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    Page<MovieDTO> list = service.findAllPaged(pageRequest);
    return ResponseEntity.ok().body(list);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<MovieDTO> findById(@PathVariable Long id) {
    MovieDTO movie = service.findById(id);
    return ResponseEntity.ok().body(movie);
  }
}
