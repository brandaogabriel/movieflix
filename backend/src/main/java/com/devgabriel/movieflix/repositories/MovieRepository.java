package com.devgabriel.movieflix.repositories;

import com.devgabriel.movieflix.entities.Genre;
import com.devgabriel.movieflix.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

  @Query("SELECT obj FROM Movie obj WHERE :genre IS NULL OR obj.genre = :genre")
  Page<Movie> find(Genre genre, Pageable pageable);
}
