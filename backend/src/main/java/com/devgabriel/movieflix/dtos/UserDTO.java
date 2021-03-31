package com.devgabriel.movieflix.dtos;

import com.devgabriel.movieflix.entities.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable {

  private Long id;
  private String name;
  private String email;

  private List<RoleDTO> roles = new ArrayList<>();

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
