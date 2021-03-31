package com.devgabriel.movieflix.services;

import com.devgabriel.movieflix.entities.User;
import com.devgabriel.movieflix.repositories.UserRepository;
import com.devgabriel.movieflix.services.exceptions.ForbiddenException;
import com.devgabriel.movieflix.services.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  @Autowired
  private UserRepository repository;

  @Transactional(readOnly = true)
  public User authenticated() {
    try {
      String username = SecurityContextHolder.getContext().getAuthentication().getName();
      return repository.findByEmail(username);
    } catch (Exception e) {
      throw new UnauthorizedException("Invalid User");
    }
  }

  public void validateSelfOrAdminAndMember(Long userId) {
    User user = authenticated();
    if(!user.getId().equals(userId) && (!user.hasHole("ROLE_ADMIN") || !user.hasHole("ROLE_MEMBER")))
      throw new ForbiddenException("Access denied");
  }
}
