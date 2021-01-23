package com.jakub.samplesecurity.repository;

import com.jakub.samplesecurity.model.Role;
import com.jakub.samplesecurity.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Set<User> findByRolesIn(Set<Role> roles);


}
