package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.MovieDTO;
import com.devgabriel.movieflix.resources.exceptions.OAuthCustomError;
import com.devgabriel.movieflix.services.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/movies")
@Api(tags = "Movie Resource")
public class MovieResource {

  @Autowired
  private MovieService service;

  @ApiOperation(value = "View all movies")
  @ApiResponses({
          @ApiResponse(code = 200, message = "ok"),
          @ApiResponse(code = 401, message = "unauthorized", response = OAuthCustomError.class),
          @ApiResponse(code = 403, message = "forbidden", response = OAuthCustomError.class)
  })
  @GetMapping(value = "/api/test")
  public ResponseEntity<List<MovieDTO>> findAll() {
    List<MovieDTO> movies = service.findAll();
    return ResponseEntity.ok().body(movies);
  }

  @ApiOperation(value = "View all Movies Paged")
  @ApiResponses({
          @ApiResponse(code = 200, message = "ok"),
          @ApiResponse(code = 401, message = "unauthorized", response = OAuthCustomError.class),
          @ApiResponse(code = 403, message = "forbidden", response = OAuthCustomError.class)
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

  @ApiOperation(value = "View a movie by id")
  @ApiResponses({
          @ApiResponse(code = 200, message = "ok"),
          @ApiResponse(code = 401, message = "unauthorized", response = OAuthCustomError.class),
          @ApiResponse(code = 403, message = "forbidden", response = OAuthCustomError.class)
  })
  @GetMapping(value = "/{id}")
  public ResponseEntity<MovieDTO> findById(@PathVariable Long id) {
    MovieDTO movie = service.findById(id);
    return ResponseEntity.ok().body(movie);
  }
}
