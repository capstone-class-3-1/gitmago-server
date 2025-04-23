package com.example.gitmago.email.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "email_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Email{

    @Id
    private String id;

    private String email;
    private int code;

    private LocalDateTime expireAt;

    private boolean verified;
}
