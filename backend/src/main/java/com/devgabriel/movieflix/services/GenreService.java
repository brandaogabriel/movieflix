package com.devgabriel.movieflix.services;

import com.devgabriel.movieflix.dtos.GenreDTO;
import com.devgabriel.movieflix.entities.Genre;
import com.devgabriel.movieflix.repositories.GenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {
  private static final Logger LOG = LoggerFactory.getLogger(GenreService.class);

  @Autowired
  private GenreRepository repository;

  @Transactional(readOnly = true)
  public List<GenreDTO> findAllGenres() {
    LOG.info("method=findAllGenres, msg=find all genres");
    List<Genre> genres = repository.findAll();
    return genres.stream()
            .map(GenreDTO::new)
            .collect(Collectors.toList());
  }

}
