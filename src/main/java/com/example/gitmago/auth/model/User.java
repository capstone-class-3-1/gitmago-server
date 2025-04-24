package com.example.gitmago.auth.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;

    private String school;

    @Indexed(unique = true)
    private String email;

    private boolean emailVerified;

    private int verificationCode;

    private LocalDateTime expireAt;
}
