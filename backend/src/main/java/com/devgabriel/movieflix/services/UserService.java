package com.devgabriel.movieflix.services;

import com.devgabriel.movieflix.entities.User;
import com.devgabriel.movieflix.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    LOG.info("method=loadUserByUserName, msg=load user by email {}", username);
    User user = repository.findByEmail(username);

    if (user == null) {
      LOG.info("method=loadUserByUserName, msg=user with email {} not found", username);
      throw new UsernameNotFoundException("Email not found");
    }
    return user;
  }
}
