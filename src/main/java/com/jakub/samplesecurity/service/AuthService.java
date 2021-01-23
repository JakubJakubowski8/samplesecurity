package com.jakub.samplesecurity.service;

import com.jakub.samplesecurity.exception.ResourceNotFoundException;
import com.jakub.samplesecurity.model.ConfirmationToken;
import com.jakub.samplesecurity.model.User;
import com.jakub.samplesecurity.payload.LoginRequest;
import com.jakub.samplesecurity.payload.ResetPasswordRequest;
import com.jakub.samplesecurity.repository.ConfirmationTokenRepository;
import com.jakub.samplesecurity.repository.UserRepository;
import com.jakub.samplesecurity.security.JwtTokenProvider;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final ConfirmationTokenRepository confirmationTokenRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;

  public AuthService(AuthenticationManager authenticationManager,
                     ConfirmationTokenRepository confirmationTokenRepository,
                     UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     JwtTokenProvider tokenProvider) {

    this.authenticationManager = authenticationManager;
    this.confirmationTokenRepository = confirmationTokenRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenProvider = tokenProvider;
  }

  public String authenticateUser(LoginRequest loginRequest) {

    Authentication auth = getAuth(loginRequest.getUsername(), loginRequest.getPassword());

    return tokenProvider.generateToken(auth);

  }

  public ConfirmationToken createConfirmationToken(String username) {

    User existingUser = findUser(username);

    ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);

    return confirmationTokenRepository.save(confirmationToken);

  }

  public Boolean resetPassword(String token, ResetPasswordRequest resetPasswordRequest) {

    ConfirmationToken confirmationToken = findToken(token);

    User user = confirmationToken.getUser();
    user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));

    userRepository.save(user);
    confirmationTokenRepository.delete(confirmationToken);
    return true;
  }

  private Authentication getAuth(String username, String password) {

    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            username,
            password
        )
    );

    SecurityContextHolder.getContext().setAuthentication(auth);
    return auth;
  }

  private User findUser(String username) {

    return
        userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
  }

  private ConfirmationToken findToken(String token) {

    return confirmationTokenRepository.findByConfirmationToken(token)
        .orElseThrow(() -> new ResourceNotFoundException("ConfirmationToken", "token", token));
  }
}
