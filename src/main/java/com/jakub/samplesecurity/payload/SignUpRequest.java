package com.jakub.samplesecurity.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class SignUpRequest {

  @NotBlank
  @Size(min = 3, max = 15)
  private String username;

  @NotBlank
  @Size(min = 6, max = 20)
  private String password;
}
