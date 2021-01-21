package com.jakub.samplesecurity.controller;

import com.jakub.samplesecurity.exception.ResourceNotFoundException;
import com.jakub.samplesecurity.model.User;
import com.jakub.samplesecurity.payload.UserIdentityAvailability;
import com.jakub.samplesecurity.payload.UserProfile;
import com.jakub.samplesecurity.repository.UserRepository;
import com.jakub.samplesecurity.security.CurrentUser;
import com.jakub.samplesecurity.security.UserPrincipal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

  private UserRepository userRepository;

  @Autowired
  public UserController(UserRepository userRepository) {

    this.userRepository = userRepository;
  }

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @GetMapping("/user/me")
  @PreAuthorize("hasRole('ADMIN')")
  public UserProfile getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    return new UserProfile(currentUser.getId(), currentUser.getUsername());
  }

  @GetMapping("/user/checkUsernameAvailability")
  @PreAuthorize("isAnonymous()")
  public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
    Boolean isAvailable = !userRepository.existsByUsername(username);
    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/users/{username}")
  @PreAuthorize("hasRole('ADMIN')")
  public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    return new UserProfile(user.getId(), user.getUsername(), user.getCreatedAt());
  }

}
