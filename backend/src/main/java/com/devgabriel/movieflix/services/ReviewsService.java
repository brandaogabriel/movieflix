package com.devgabriel.movieflix.services;

import com.devgabriel.movieflix.dtos.ReviewDTO;
import com.devgabriel.movieflix.entities.Movie;
import com.devgabriel.movieflix.entities.Review;
import com.devgabriel.movieflix.entities.User;
import com.devgabriel.movieflix.repositories.MovieRepository;
import com.devgabriel.movieflix.repositories.ReviewRepository;
import com.devgabriel.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReviewsService {

  @Autowired
  private ReviewRepository repository;

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private AuthService authService;


  @Transactional
  public ReviewDTO insertReview(ReviewDTO dto) {
     User user = authService.authenticated();
     authService.validateSelfOrAdminAndMember(user.getId());

     Optional<Movie> obj = movieRepository.findById(dto.getMovieId());
     Movie movie = obj.orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + dto.getMovieId()));

     Review review = new Review();
     review.setText(dto.getText());
     review.setMovie(movie);
     review.setUser(user);

     review = repository.save(review);
     return new ReviewDTO(review);
  }
}
