package com.jakub.samplesecurity.controller;

import com.jakub.samplesecurity.model.Role;
import com.jakub.samplesecurity.payload.ApiResponse;
import com.jakub.samplesecurity.payload.RoleRequest;
import com.jakub.samplesecurity.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class RoleController {

  private final RoleService roleService;

  @Autowired
  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }


  @PostMapping("/role/create")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequest roleRequest) {

    Role role = roleService.createRole(roleRequest);

    return new ResponseEntity<>(new ApiResponse(true, "Role created successfully!", ""),
        HttpStatus.CREATED);
  }

  @PutMapping("/role/update")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> updateRole(@Valid @RequestBody RoleRequest roleRequest) {

    Role role = roleService.updateRole(roleRequest);

    return new ResponseEntity<>(new ApiResponse(true, "Role updated successfully!", ""),
        HttpStatus.OK);
  }

  @DeleteMapping("/role/delete/{name}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> updateUser(@PathVariable(value = "name") String name) {

    roleService.deleteRole(name);

    return new ResponseEntity<>(new ApiResponse(true, "Role deleted successfully!", ""),
        HttpStatus.OK);
  }

  @GetMapping("/role/all")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getAllRoles(Pageable pageable) {

    Page<Role> rolePage = roleService.findAllRoles(pageable);

    return new ResponseEntity<>(rolePage,
        HttpStatus.OK);
  }

}
