package com.devgabriel.movieflix.resources.exceptions;

import com.devgabriel.movieflix.services.exceptions.ForbiddenException;
import com.devgabriel.movieflix.services.exceptions.ResourceNotFoundException;
import com.devgabriel.movieflix.services.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    StandardError err = createStandardError(status, e.getMessage(), "Resource not found", request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<StandardError> illegalArgument(IllegalArgumentException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    StandardError err = createStandardError(status, e.getMessage(), "Bad request", request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  private StandardError createStandardError(HttpStatus status, String error, String message, String requestUri) {
    return new StandardError(Instant.now(), status.value(), error, message, requestUri);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<OAuthCustomError> forbidden(ForbiddenException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.FORBIDDEN;
    OAuthCustomError err = new OAuthCustomError("Forbidden", e.getMessage());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<OAuthCustomError> unauthorized(UnauthorizedException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.UNAUTHORIZED;
    OAuthCustomError err = new OAuthCustomError("Unauthorized", e.getMessage());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    ValidationError err = new ValidationError();
    err.setTimeStamp(Instant.now());
    err.setStatus(status.value());
    err.setError("Validation exception");
    err.setMessage(e.getMessage());
    err.setPath(request.getRequestURI());

    for(FieldError f : e.getBindingResult().getFieldErrors())
      err.addError(f.getField(), f.getDefaultMessage());

    return ResponseEntity.status(status).body(err);
  }
}
