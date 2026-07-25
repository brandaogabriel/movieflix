package com.devgabriel.movieflix.services;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.duration:86400}")
  private Long jwtDuration;

  private SecretKey getSecretKey() {
    return new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withSecretKey(getSecretKey()).build();
  }

  public String generateToken(Authentication authentication) {
    try {
      Instant now = Instant.now();
      Instant expiry = now.plusSeconds(jwtDuration);

      List<String> roles = authentication.getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .toList();

      JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
          .subject(authentication.getName())
          .issueTime(Date.from(now))
          .expirationTime(Date.from(expiry))
          .claim("roles", roles)
          .build();

      SignedJWT signedJWT = new SignedJWT(
          new JWSHeader(JWSAlgorithm.HS256),
          claimsSet
      );

      JWSSigner signer = new MACSigner(getSecretKey());
      signedJWT.sign(signer);

      return signedJWT.serialize();
    } catch (Exception e) {
      throw new RuntimeException("Error generating JWT token", e);
    }
  }

  public Long getJwtDuration() {
    return jwtDuration;
  }
}
