package com.jakub.samplesecurity.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ForgotPasswordRequest {
  @NotBlank
  private String username;
}
