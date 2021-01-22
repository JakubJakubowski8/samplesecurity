package com.jakub.samplesecurity.controller;

import com.jakub.samplesecurity.exception.ResourceNotFoundException;
import com.jakub.samplesecurity.model.ConfirmationToken;
import com.jakub.samplesecurity.payload.ResetPasswordRequest;
import com.jakub.samplesecurity.model.User;
import com.jakub.samplesecurity.payload.ApiResponse;
import com.jakub.samplesecurity.payload.ForgotPasswordRequest;
import com.jakub.samplesecurity.payload.JwtAuthenticationResponse;
import com.jakub.samplesecurity.payload.LoginRequest;
import com.jakub.samplesecurity.payload.UserRequest;
import com.jakub.samplesecurity.repository.ConfirmationTokenRepository;
import com.jakub.samplesecurity.repository.RoleRepository;
import com.jakub.samplesecurity.repository.UserRepository;
import com.jakub.samplesecurity.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final ConfirmationTokenRepository confirmationTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;

  @Autowired
  public AuthController(AuthenticationManager authenticationManager,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtTokenProvider tokenProvider,
                        ConfirmationTokenRepository confirmationTokenRepository) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenProvider = tokenProvider;
    this.confirmationTokenRepository = confirmationTokenRepository;
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
    if(userRepository.existsByUsername(userRequest.getUsername())) {
      return new ResponseEntity(new ApiResponse(false, "Username is already taken!", ""),
          HttpStatus.BAD_REQUEST);
    }

    User user = new User();

    user.setUsername(userRequest.getUsername());
    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

    User result = userRepository.save(user);

    URI location = ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/users/{username}")
        .buildAndExpand(result.getUsername()).toUri();

    return ResponseEntity.created(location).body(new ApiResponse(true, "User registered " +
        "successfully", ""));
  }

  @PostMapping(value="/forgot-password")
  public ResponseEntity<?> forgotUserPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {

    User existingUser = userRepository.findByUsername(forgotPasswordRequest.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("User", "username",
            forgotPasswordRequest.getUsername()));

      ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);

      confirmationTokenRepository.save(confirmationToken);

    return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully! Send request " +
        "with new password to bellow url",
        "http://localhost:8080/api/auth/reset-password?token=" + confirmationToken.getConfirmationToken()));
  }

  @PostMapping(value="/reset-password")
  public ResponseEntity<?> forgotUserPassword(@Valid @RequestParam("token") String token,
                                              @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

    ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token)
        .orElseThrow(() -> new ResourceNotFoundException("Forgot password token", "token",
            token));
    User user = confirmationToken.getUser();
    user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));

    userRepository.save(user);
    confirmationTokenRepository.delete(confirmationToken);

    return ResponseEntity.ok(new ApiResponse(true, "Password reseted successfully!", ""));
  }
}
