package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.ReviewDTO;
import com.devgabriel.movieflix.services.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/reviews")
public class ReviewResource {

  @Autowired
  private ReviewsService service;

  @PostMapping
  public ResponseEntity<ReviewDTO> insert(@RequestBody ReviewDTO dto) {
    dto = service.insertReview(dto);
    URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(dto.getId())
            .toUri();

    return ResponseEntity.created(uri).body(dto);
  }
}
