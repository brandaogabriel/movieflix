package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.ReviewDTO;
import com.devgabriel.movieflix.resources.exceptions.OAuthCustomError;
import com.devgabriel.movieflix.services.ReviewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/reviews")
@Tag(name = "Review Resource")
public class ReviewResource {

  @Autowired
  private ReviewsService service;

  @Operation(summary = "Create a review in a movie")
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "created", content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
          @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content(schema = @Schema(implementation = OAuthCustomError.class))),
          @ApiResponse(responseCode = "403", description = "forbidden", content = @Content(schema = @Schema(implementation = OAuthCustomError.class)))
  })
  @PostMapping
  public ResponseEntity<ReviewDTO> insert(@Valid @RequestBody ReviewDTO dto) {
    dto = service.insertReview(dto);
    URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(dto.getId())
            .toUri();

    return ResponseEntity.created(uri).body(dto);
  }
}
