package com.jakub.samplesecurity.controller;

import com.jakub.samplesecurity.model.User;
import com.jakub.samplesecurity.payload.ApiResponse;
import com.jakub.samplesecurity.payload.UserRequest;
import com.jakub.samplesecurity.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/user/create")
  @PreAuthorize("hasRole('CREATE_USER')")
  public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest) {

    User user = userService.createUser(userRequest);

    return new ResponseEntity<>(new ApiResponse(true, "User registered successfully!", ""),
        HttpStatus.CREATED);
  }

  @PutMapping("/user/update")
  @PreAuthorize("hasRole('UPDATE_USER')")
  public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequest userRequest) {

    User user = userService.updateUser(userRequest);

    return new ResponseEntity<>(new ApiResponse(true, "User updated successfully!", ""),
        HttpStatus.OK);
  }

  @DeleteMapping("/user/delete/{id}")
  @PreAuthorize("hasRole('DELETE_USER')")
  public ResponseEntity<?> updateUser(@PathVariable(value = "id") Long id) {

    userService.deleteUser(id);

    return new ResponseEntity<>(new ApiResponse(true, "User deleted successfully!", ""),
        HttpStatus.OK);
  }

  @GetMapping("/user/all")
  @PreAuthorize("hasRole('LIST_USER')")
  public ResponseEntity<?> getAllUsers(Pageable pageable) {

    Page<User> userPage = userService.findAllUsers(pageable);
    userPage.getContent();
    return new ResponseEntity<>(userPage,
        HttpStatus.OK);
  }
}
