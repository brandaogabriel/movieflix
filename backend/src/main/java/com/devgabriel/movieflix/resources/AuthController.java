package com.devgabriel.movieflix.resources;

import com.devgabriel.movieflix.dtos.LoginRequestDTO;
import com.devgabriel.movieflix.dtos.TokenResponseDTO;
import com.devgabriel.movieflix.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @PostMapping(value = "/login")
  public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    String token = jwtService.generateToken(authentication);
    TokenResponseDTO response = new TokenResponseDTO(token, "Bearer", jwtService.getJwtDuration());

    return ResponseEntity.ok(response);
  }
}
