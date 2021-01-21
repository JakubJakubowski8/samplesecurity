package com.jakub.samplesecurity.repository;

import com.jakub.samplesecurity.model.Role;
import com.jakub.samplesecurity.model.RoleName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(RoleName roleName);
}
