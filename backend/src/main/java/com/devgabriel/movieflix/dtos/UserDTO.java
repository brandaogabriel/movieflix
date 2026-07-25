package com.devgabriel.movieflix.dtos;

import com.devgabriel.movieflix.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable {

  @Schema(example = "1")
  private Long id;

  @Schema(example = "Bob Brown")
  private String name;

  @Schema(example = "bob@gmail.com")
  private String email;

  @Schema
  private final List<RoleDTO> roles = new ArrayList<>();

  public UserDTO() {
  }

  public UserDTO(Long id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  public UserDTO(User entity) {
    id = entity.getId();
    name = entity.getName();
    email = entity.getEmail();
    entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<RoleDTO> getRoles() {
    return roles;
  }
}
