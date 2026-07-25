package com.devgabriel.movieflix.dtos;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public class LoginRequestDTO implements Serializable {

  @NotBlank(message = "Campo obrigatório")
  private String email;

  @NotBlank(message = "Campo obrigatório")
  private String password;

  public LoginRequestDTO() {
  }

  public LoginRequestDTO(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
