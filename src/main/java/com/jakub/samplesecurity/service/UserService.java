package com.jakub.samplesecurity.service;

import com.jakub.samplesecurity.exception.ResourceNotFoundException;
import com.jakub.samplesecurity.exception.UsernameTakenException;
import com.jakub.samplesecurity.model.Role;
import com.jakub.samplesecurity.model.User;
import com.jakub.samplesecurity.payload.UserRequest;
import com.jakub.samplesecurity.repository.RoleRepository;
import com.jakub.samplesecurity.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User createUser(UserRequest userRequest) {

    checkIfUsernameTaken(userRequest);
    return saveUser(userRequest);

  }

  public User updateUser(UserRequest userRequest) {

    User user = findUserByUsername(userRequest.getUsername());
    return saveUser(user, userRequest);
  }

  public void deleteUser(String username) {

    User user = findUserByUsername(username);
    userRepository.delete(user);
  }

  public Page<User> findAllUsers(Pageable pageable) {

    return userRepository.findAll(pageable);
  }

  private User saveUser(UserRequest userRequest) {

    return saveUser(new User(), userRequest);
  }

  private User saveUser(User user, UserRequest userRequest) {
    Set<Role> roles = roleRepository.findByNameIn(userRequest.getRoles());

    user.setUsername(userRequest.getUsername());
    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
    user.setRoles(roles);

    return userRepository.save(user);
  }

  private User findUserByUsername(String username) {

    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
  }

  private void checkIfUsernameTaken(UserRequest userRequest) {

    if(userRepository.existsByUsername(userRequest.getUsername())) {
      throw new UsernameTakenException(userRequest.getUsername());
    }
  }
}
