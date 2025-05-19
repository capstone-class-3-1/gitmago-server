package com.example.gitmago.github;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubCommitService {

    private final RestTemplate restTemplate = new RestTemplate();

    public int getPublicCommitCount(String githubAccessToken) {
        String githubId = getGithubUsername(githubAccessToken);
        String query = buildContributionQuery(githubId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(query, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                "https://api.github.com/graphql",
                HttpMethod.POST,
                request,
                JsonNode.class
        );

        return response.getBody()
                .path("data")
                .path("user")
                .path("contributionsCollection")
                .path("contributionCalendar")
                .path("totalContributions")
                .asInt();
    }
    //github 사용자 이름 가지고옴
    private String getGithubUsername(String githubAccessToken) {
        String query = """
        {
          "query": "query { viewer { login } }"
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(query, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                "https://api.github.com/graphql",
                HttpMethod.POST,
                request,
                JsonNode.class
        );

        return response.getBody().path("data").path("viewer").path("login").asText();
    }

    private String buildContributionQuery(String githubId) {
        return String.format("""
        {
          "query": "query { user(login: \\"%s\\") { contributionsCollection { contributionCalendar { totalContributions } } } }"
        }
        """, githubId);
    }
}
