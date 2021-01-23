package com.jakub.samplesecurity.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ResetPasswordRequest {

  @NotBlank
  private String password;
}
