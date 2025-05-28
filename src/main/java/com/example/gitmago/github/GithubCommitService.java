package com.example.gitmago.github;

import com.example.gitmago.title.TitleCommitService;
import com.example.gitmago.user.User;
import com.example.gitmago.user.UserRepository;
import com.example.gitmago.jwt.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GithubCommitService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TitleCommitService titleService;

    private final RestTemplate restTemplate = new RestTemplate();

    // 유저의 public 커밋 수 조회
    public int getPublicCommitCount(String githubUsername) {
        String query = buildContributionQuery(githubUsername);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setBearerAuth("ghp_...");

        HttpEntity<String> request = new HttpEntity<>(query, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                "https://api.github.com/graphql",
                HttpMethod.POST,
                request,
                JsonNode.class
        );

        JsonNode body = response.getBody();
        if (body == null || body.path("data").path("user").isMissingNode()) {
            throw new RuntimeException("GitHub API 응답 오류: user 정보 없음");
        }

        return body.path("data")
                .path("user")
                .path("contributionsCollection")
                .path("contributionCalendar")
                .path("totalContributions")
                .asInt();
    }

    // 로그인한 사용자 기준으로 커밋 수 업데이트 + 칭호 부여
    public User updateCommitInfoAndGrantTitles(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow();

        int commitCount = getPublicCommitCount(user.getGithubUsername());
        user.setPublicCommitCount(commitCount);

        // 커밋 수 기준 칭호 부여
        titleService.grantCommitTitleByCount(user);

        return userRepository.save(user);
    }

    // 사용자 login 조회용 GraphQL
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

    // 커밋 수 조회용 쿼리 생성
    private String buildContributionQuery(String githubId) {
        return """
        {
          "query": "query { user(login: \\"%s\\") { contributionsCollection { contributionCalendar { totalContributions } } } }"
        }
        """.formatted(githubId);
    }
}
