package com.devgabriel.movieflix.services;

import com.devgabriel.movieflix.dtos.GenreDTO;
import com.devgabriel.movieflix.entities.Genre;
import com.devgabriel.movieflix.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

  @Autowired
  private GenreRepository repository;

  @Transactional(readOnly = true)
  public List<GenreDTO> findAllGenres() {
    List<Genre> genres = repository.findAll();
    return genres.stream()
            .map(GenreDTO::new)
            .collect(Collectors.toList());
  }

}
