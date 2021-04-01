package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.GenreDTO;
import com.devgabriel.movieflix.resources.exceptions.OAuthCustomError;
import com.devgabriel.movieflix.services.GenreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/genres")
@Api(tags = "Genre Resource")
public class GenreResource {

  @Autowired
  private GenreService service;

  @ApiOperation(value = "View all Genres")
  @ApiResponses({
          @ApiResponse(code = 200, message = "ok"),
          @ApiResponse(code = 401, message = "unauthorized", response = OAuthCustomError.class),
          @ApiResponse(code = 403, message = "forbidden", response = OAuthCustomError.class)
  })
  @GetMapping
  public ResponseEntity<List<GenreDTO>> findAllGenres() {
    return ResponseEntity.ok().body(service.findAllGenres());
  }
}
