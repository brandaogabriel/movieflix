package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.MovieDTO;
import com.devgabriel.movieflix.resources.exceptions.OAuthCustomError;
import com.devgabriel.movieflix.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/movies")
@Tag(name = "Movie Resource")
public class MovieResource {

  @Autowired
  private MovieService service;

  @Operation(summary = "View all movies")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "ok"),
          @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content(schema = @Schema(implementation = OAuthCustomError.class))),
          @ApiResponse(responseCode = "403", description = "forbidden", content = @Content(schema = @Schema(implementation = OAuthCustomError.class)))
  })
  @GetMapping(value = "/api/test")
  public ResponseEntity<List<MovieDTO>> findAll() {
    List<MovieDTO> movies = service.findAll();
    return ResponseEntity.ok().body(movies);
  }

  @Operation(summary = "View all Movies Paged")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "ok"),
          @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content(schema = @Schema(implementation = OAuthCustomError.class))),
          @ApiResponse(responseCode = "403", description = "forbidden", content = @Content(schema = @Schema(implementation = OAuthCustomError.class)))
  })
  @GetMapping
  public ResponseEntity<Page<MovieDTO>> findAllPaged(
          @RequestParam(value = "genreId", defaultValue = "0") Long genreId,
          @RequestParam(value = "page", defaultValue = "0") Integer page,
          @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
          @RequestParam(value = "direction", defaultValue = "ASC") String direction,
          @RequestParam(value = "orderBy", defaultValue = "title") String orderBy
  ) {

    PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    Page<MovieDTO> list = service.findAllPaged(genreId, pageRequest);
    return ResponseEntity.ok().body(list);
  }

  @Operation(summary = "View a movie by id")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "ok"),
          @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content(schema = @Schema(implementation = OAuthCustomError.class))),
          @ApiResponse(responseCode = "403", description = "forbidden", content = @Content(schema = @Schema(implementation = OAuthCustomError.class)))
  })
  @GetMapping(value = "/{id}")
  public ResponseEntity<MovieDTO> findById(@PathVariable Long id) {
    MovieDTO movie = service.findById(id);
    return ResponseEntity.ok().body(movie);
  }
}
