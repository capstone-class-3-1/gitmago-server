package com.example.gitmago.auth.repository;

import com.example.gitmago.auth.model.Auth;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface AuthRepository extends MongoRepository<Auth, String> {
    Optional<Auth> findByUsername(String username);
    Optional<Auth> findByEmail(String email);
}
