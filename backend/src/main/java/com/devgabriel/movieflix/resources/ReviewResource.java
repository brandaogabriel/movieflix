package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.ReviewDTO;
import com.devgabriel.movieflix.resources.exceptions.OAuthCustomError;
import com.devgabriel.movieflix.services.ReviewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/reviews")
@Api(tags = "Review Resource")
public class ReviewResource {

  @Autowired
  private ReviewsService service;

  @ApiOperation(value = "Create a review in a movie")
  @ApiResponses({
          @ApiResponse(code = 201, message = "created", response = ReviewDTO.class),
          @ApiResponse(code = 401, message = "unauthorized", response = OAuthCustomError.class),
          @ApiResponse(code = 403, message = "forbidden", response = OAuthCustomError.class)
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
