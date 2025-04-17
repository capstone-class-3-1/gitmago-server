package com.example.gitmago.email.repository;

import com.example.gitmago.email.model.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends MongoRepository<Email, String> {
    Optional<Email> findTopByEmailOrderByExpireAtDesc(String email);
}
