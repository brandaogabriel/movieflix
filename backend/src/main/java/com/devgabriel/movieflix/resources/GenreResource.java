package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.GenreDTO;
import com.devgabriel.movieflix.resources.exceptions.OAuthCustomError;
import com.devgabriel.movieflix.services.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/genres")
@Tag(name = "Genre Resource")
public class GenreResource {

  @Autowired
  private GenreService service;

  @Operation(summary = "View all Genres")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "ok"),
          @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content(schema = @Schema(implementation = OAuthCustomError.class))),
          @ApiResponse(responseCode = "403", description = "forbidden", content = @Content(schema = @Schema(implementation = OAuthCustomError.class)))
  })
  @GetMapping
  public ResponseEntity<List<GenreDTO>> findAllGenres() {
    return ResponseEntity.ok().body(service.findAllGenres());
  }
}
