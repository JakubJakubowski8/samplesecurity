package com.jakub.samplesecurity.controller;

import com.jakub.samplesecurity.model.ConfirmationToken;
import com.jakub.samplesecurity.payload.ResetPasswordRequest;
import com.jakub.samplesecurity.payload.ApiResponse;
import com.jakub.samplesecurity.payload.JwtAuthenticationResponse;
import com.jakub.samplesecurity.payload.LoginRequest;
import com.jakub.samplesecurity.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @Autowired
  public AuthController(AuthService authService) {

    this.authService = authService;
  }

  /**
   * Login to app.
   *
   * @param loginRequest user credentials
   * @return authentication token
   */
  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    String jwt = authService.authenticateUser(loginRequest);

    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
  }

  /**
   * Forgot password endpoint
   *
   * @param username user name
   * @return confirmation token
   */
  @PostMapping(value="/forgot-password/{username}")
  public ResponseEntity<?> forgotUserPassword(@Valid @PathVariable(value = "username") String username) {

    ConfirmationToken confToken =
        authService.createConfirmationToken(username);

    return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully! Send request " +
        "with new password to bellow url",
        "http://localhost:8080/api/auth/reset-password?token=" + confToken.getConfirmationToken()));
  }

  /**
   * Reset password endpoint
   *
   * @param token confirmation token
   * @param resetPasswordRequest new user password
   * @return confirmation
   */
  @PostMapping(value="/reset-password")
  public ResponseEntity<?> forgotUserPassword(@Valid @RequestParam("token") String token,
                                              @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

    if (authService.resetPassword(token, resetPasswordRequest)){
      return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully!", ""));
    }
    else {
      return ResponseEntity.badRequest().body(new ApiResponse(false, "Password didn't reset",""));
    }
  }
}
