package com.jakub.samplesecurity.repository;

import com.jakub.samplesecurity.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Boolean existsByName(String roleName);
}
