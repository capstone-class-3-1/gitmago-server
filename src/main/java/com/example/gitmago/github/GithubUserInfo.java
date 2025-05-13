package com.example.gitmago.github;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GithubUserInfo {
    private String id;
    private String login;
    private String email;
    private String avatarUrl;
}
