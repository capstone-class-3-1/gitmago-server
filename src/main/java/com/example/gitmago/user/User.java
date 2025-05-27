package com.example.gitmago.user;

import com.example.gitmago.title.Title;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private String githubId;

    private String githubUsername;

    private String githubEmail;

    private String githubAvatar;

    private List<Title> titles = new ArrayList<>(); //획득한 칭호
    private Title equippedTitle; //지금 착용한(?) 칭호
}
