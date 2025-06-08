package com.example.gitmago.github;

import com.example.gitmago.user.User;
import com.example.gitmago.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final MongoTemplate mongoTemplate;
    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    private final UserRepository userRepository;


    public String processGithubOAuth(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // 액세스 토큰 요청 (사용자 계정인지 인식하는 단계)
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

        // 사용자 정보 요청 DB에 저장할 사용자 정보들을 github를 통해서 가지고옴
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
        String githubEmail = userData.get("email").asText(null);

        Optional<User> optionalUser = userRepository.findByGithubId(githubId);

        if (optionalUser.isPresent()) {
            Query query = new Query(Criteria.where("githubId").is(githubId));
            Update update = new Update()
                    .set("githubUsername", githubUsername)
                    .set("githubEmail", githubEmail)
                    .set("githubAvatar", githubAvatar);

            mongoTemplate.updateFirst(query, update, User.class);
        } else {
            User user = User.builder()
                    .githubId(githubId)
                    .githubUsername(githubUsername)
                    .githubEmail(githubEmail)
                    .githubAvatar(githubAvatar)
                    .emailVerified(false)
                    .githubAccessToken(accessToken)
                    .build();

            userRepository.save(user);
        }

        return githubUsername + "계정 아이디";
    }
}
