package com.jakub.samplesecurity.payload;

import com.jakub.samplesecurity.model.RightName;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RoleRequest {

  @NotBlank
  @Size(min = 3, max = 30)
  private String roleName;

  private Set<RightName> rights;
}
