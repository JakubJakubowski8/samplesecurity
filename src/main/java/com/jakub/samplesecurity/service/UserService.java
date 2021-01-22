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
import java.util.stream.Collectors;

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

    User user = userRepository.findByUsername(userRequest.getUsername())
        .orElse(createUser(userRequest));

    return userRepository.save(user);
  }

  public void deleteUser(Long id) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

    userRepository.delete(user);
  }


  private User saveUser(UserRequest userRequest) {

    return saveUser(new User(), userRequest);
  }

  private User saveUser(User user, UserRequest userRequest) {
    Set<Role> roles = findRoles(userRequest.getRoles());

    user.setUsername(userRequest.getUsername());
    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
    user.setRoles(roles);

    return userRepository.save(user);
  }

  private Set<Role> findRoles(Set<String> roleNames) {
    return
        roleNames.stream().map(
            role -> roleRepository.findByName(role)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "role", role)))
            .collect(Collectors.toSet());
  }

  private void checkIfUsernameTaken(UserRequest userRequest) {
    if(userRepository.existsByUsername(userRequest.getUsername())) {
      throw new UsernameTakenException(userRequest.getUsername());
    }
  }

  public Page<User> findAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }
}
