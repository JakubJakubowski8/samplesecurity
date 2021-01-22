package com.jakub.samplesecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameTakenException extends RuntimeException {

  @Getter
  private String username;

  public UsernameTakenException( String username) {
    super(String.format("Username %s is already taken!", username));
    this.username = username;
  }
}
