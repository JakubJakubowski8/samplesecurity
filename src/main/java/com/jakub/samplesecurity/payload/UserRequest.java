package com.jakub.samplesecurity.payload;

import com.jakub.samplesecurity.model.RoleName;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import javax.validation.constraints.*;

@Getter
@Setter
public class UserRequest {

  @NotBlank
  @Size(min = 3, max = 15)
  private String username;

  @NotBlank
  @Size(min = 6, max = 20)
  private String password;

  private Set<String> roles;
}
