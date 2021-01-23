package com.jakub.samplesecurity.service;

import com.jakub.samplesecurity.exception.ResourceNotFoundException;
import com.jakub.samplesecurity.exception.RoleInUseException;
import com.jakub.samplesecurity.exception.UsernameTakenException;
import com.jakub.samplesecurity.model.Right;
import com.jakub.samplesecurity.model.Role;
import com.jakub.samplesecurity.payload.RoleRequest;
import com.jakub.samplesecurity.repository.RightRepository;
import com.jakub.samplesecurity.repository.RoleRepository;
import com.jakub.samplesecurity.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class RoleService {

  private final RoleRepository roleRepository;
  private final RightRepository rightRepository;
  private final UserRepository userRepository;

  @Autowired
  public RoleService(RoleRepository roleRepository, RightRepository rightRepository, UserRepository userRepository) {
    this.roleRepository = roleRepository;
    this.rightRepository = rightRepository;
    this.userRepository = userRepository;
  }

  public Role createRole(RoleRequest roleRequest) {

    checkIfRoleNameTaken(roleRequest);
    return saveRole(roleRequest);
  }

  public Role updateRole(RoleRequest roleRequest) {

    Role role = findRoleByName(roleRequest.getRoleName());
    return saveRole(role, roleRequest);
  }

  public void deleteRole(String name) {

    Role role = findRoleByName(name);

    checkIfRoleInUse(role);
    roleRepository.delete(role);
  }

  public Page<Role> findAllRoles(Pageable pageable) {

    return roleRepository.findAll(pageable);
  }

  private Role saveRole(RoleRequest roleRequest) {

    return saveRole(new Role(), roleRequest);
  }

  private Role saveRole(Role role, RoleRequest roleRequest) {

    Set<Right> rights = rightRepository.findByNameIn(roleRequest.getRights());

    role.setName(roleRequest.getRoleName());
    role.setRight(rights);

    return roleRepository.save(role);
  }

  private Role findRoleByName(String name) {

    return roleRepository.findByName(name)
        .orElseThrow(() -> new ResourceNotFoundException("Name", "name", name));
  }

  private void checkIfRoleNameTaken(RoleRequest roleRequest) {

    if(roleRepository.existsByName(roleRequest.getRoleName())) {
      throw new UsernameTakenException(roleRequest.getRoleName());
    }
  }

  private void checkIfRoleInUse(Role role) {

    if (userRepository.findByRolesIn(Collections.singleton(role)).size()>0) {
      throw new RoleInUseException(role.getName());
    }
  }
}

