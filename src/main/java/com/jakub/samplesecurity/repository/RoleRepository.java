package com.jakub.samplesecurity.repository;

import com.jakub.samplesecurity.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Boolean existsByName(String roleName);

  Optional<Role> findByName(String role);

  Set<Role> findByNameIn(Set<String> roles);
}
