package com.example.gitmago.auth.github;

import com.example.gitmago.auth.model.User;
import com.example.gitmago.auth.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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

    public String processGithubOAuth(String code) throws  Exception{
        RestTemplate restTemplate = new RestTemplate();

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

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBasicAuth(accessToken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<JsonNode> userResponse = restTemplate.exchange(
                "https://api.github.com/user", HttpMethod.GET, userRequest, JsonNode.class
        );

        JsonNode userData = userResponse.getBody();
        String githubUsername = userData.get("login").asText();
        String githubId = userData.get("id").asText();
        String githubAvatar = userData.get("avatar_url").asText();
        String githubEmail = userData.get("email").asText();

        Optional<User> optionalUser = userRepository.findByGithubId(githubId);

        User user = optionalUser.orElseGet(()-> User.builder()
                .githubId(githubId)
                .githubUsername(githubUsername)
                .githubEmail(githubEmail)
                .githubAvatar(githubAvatar)
                .build()
        );

        user.setGithubUsername(githubUsername);
        user.setGithubEmail(githubEmail);
        user.setGithubAvatar(githubAvatar);

        userRepository.save(user);

        return githubUsername + "님 로그인 성공!";




    }
}
