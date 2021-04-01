package com.devgabriel.movieflix.resources.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class OAuthCustomError implements Serializable {

  @ApiModelProperty(position = 1, example = "unauthorized")
  private String error;

  @ApiModelProperty(position = 2, example = "Full authentication is required to access this resource")
  @JsonProperty("error_description")
  private String errorDescription;

  public OAuthCustomError() {
  }

  public OAuthCustomError(String error, String errorDescription) {
    this.error = error;
    this.errorDescription = errorDescription;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }
}
