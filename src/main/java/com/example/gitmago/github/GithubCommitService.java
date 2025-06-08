package com.example.gitmago.github;

import com.example.gitmago.jwt.JwtUtil;
import com.example.gitmago.title.TitleCommitService;
import com.example.gitmago.user.User;
import com.example.gitmago.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GithubCommitService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TitleCommitService titleService;

    private final RestTemplate restTemplate = new RestTemplate();

    public int getCommitCountSince(String githubUsername, LocalDateTime fromDate, String githubToken) {
        String query = """
            {
              "query": "query {
                user(login: \\"%s\\") {
                  contributionsCollection(from: \\"%s\\") {
                    contributionCalendar {
                      totalContributions
                    }
                  }
                }
              }"
            }
        """.formatted(githubUsername, fromDate.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
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

    // 로그인한 사용자 기준으로 커밋 수 업데이트 + 칭호 부여
    public User updateCommitInfoAndGrantTitles(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow();

        int commitCount = getCommitCountSince(user.getGithubUsername(), user.getExpireAt(),user.getGithubAccessToken());

        //커밋 수만 전달하고, 내부에서 로직 처리하도록
        titleService.grantCommitTitleByCount(user, commitCount);

        return userRepository.save(user);
    }
}
