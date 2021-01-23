package com.jakub.samplesecurity.repository;

import com.jakub.samplesecurity.model.Right;
import com.jakub.samplesecurity.model.RightName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RightRepository extends JpaRepository<Right, Long> {

  Boolean existsByName(RightName rightName);

  Set<Right> findByNameIn(Set<RightName> rights);
}