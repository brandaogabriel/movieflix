package com.devgabriel.movieflix.dtos;

import com.devgabriel.movieflix.entities.Role;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class RoleDTO implements Serializable {

  @ApiModelProperty(position = 1, example = "1")
  private Long id;

  @ApiModelProperty(position = 2, example = "ROLE_MEMBER")
  private String authority;

  public RoleDTO() {
  }

  public RoleDTO(Role entity) {
    id = entity.getId();
    authority = entity.getAuthority();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }
}
