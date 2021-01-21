package com.jakub.samplesecurity.repository;

import com.jakub.samplesecurity.model.ConfirmationToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
  Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);
}