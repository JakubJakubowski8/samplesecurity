package com.jakub.samplesecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoleInUseException extends RuntimeException {

  @Getter
  private String role;

  public RoleInUseException(String role) {
    super(String.format("Role %s is in use!", role));
    this.role = role;
  }
}
