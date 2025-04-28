package com.example.gitmago.github;

import com.example.gitmago.auth.model.User;
import com.example.gitmago.auth.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GithubService {

    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    private final UserRepository userRepository;

    public String processGithubOAuth(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // 1. GitHub 액세스 토큰 요청
        String tokenUrl = "https://github.com/login/oauth/access_token";
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_JSON);
        tokenHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        String tokenBody = String.format(
                "{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"code\":\"%s\"}",
                clientId, clientSecret, code
        );

        HttpEntity<String> tokenRequest = new HttpEntity<>(tokenBody, tokenHeaders);
        ResponseEntity<JsonNode> tokenResponse = restTemplate.exchange(
                tokenUrl, HttpMethod.POST, tokenRequest, JsonNode.class
        );

        String accessToken = tokenResponse.getBody().get("access_token").asText();

        // 2. GitHub 사용자 정보 요청
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<JsonNode> userResponse = restTemplate.exchange(
                "https://api.github.com/user", HttpMethod.GET, userRequest, JsonNode.class
        );

        JsonNode userData = userResponse.getBody();
        String githubUsername = userData.get("login").asText();
        String githubId = userData.get("id").asText();
        String githubAvatar = userData.get("avatar_url").asText();
        String githubEmail = userData.hasNonNull("email") ? userData.get("email").asText() : null;

        // 3. 기존 회원 찾기
        Optional<User> optionalUser = userRepository.findByGithubId(githubId);

        if (optionalUser.isEmpty() && githubEmail != null && !githubEmail.isBlank()) {
            optionalUser = userRepository.findByEmail(githubEmail);
        }

        User user;
        if (optionalUser.isPresent()) {
            // 4. 기존 유저 있으면 GitHub 정보 업데이트
            user = optionalUser.get();

            if (user.getGithubId() == null) {
                user.setGithubId(githubId);
            }
            user.setGithubUsername(githubUsername);
            user.setGithubAvatar(githubAvatar);
            if (user.getGithubEmail() == null && githubEmail != null) {
                user.setGithubEmail(githubEmail);
            }

        } else {
            user = User.builder()
                    .githubId(githubId)
                    .githubUsername(githubUsername)
                    .githubAvatar(githubAvatar)
                    .githubEmail(githubEmail)
                    .email(githubEmail)
                    .emailVerified(true)
                    .build();
        }

        userRepository.save(user);

        return githubUsername + "님 로그인 성공!";
    }
}
