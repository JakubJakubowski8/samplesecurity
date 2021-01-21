package com.jakub.samplesecurity.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
  private Long id;
  private String username;
  private Instant joinedAt;

  public UserProfile(Long id, String username) {
    this.id = id;
    this.username = username;
  }
}
