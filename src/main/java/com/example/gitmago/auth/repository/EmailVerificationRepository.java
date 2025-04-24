package com.example.gitmago.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends MongoRepository<Email, String> {
    Optional<Email> findTopByEmailOrderByExpireAtDesc(String email);
}
