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
import java.time.LocalDateTime;

//커밋 조회 & 업데이트 TitleCommitService 랑 헷갈릴수 있음
@Service
@RequiredArgsConstructor
public class GithubCommitService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TitleCommitService titleCommitService;
    private final RestTemplate restTemplate = new RestTemplate();

    // 유저의 public 커밋 수 조회 (가입 이후부터)
    public int getCommitCountSince(String githubUsername, LocalDateTime fromDate) {
        String fromDateIso = fromDate.toString();

        String query = """
        {
          \"query\": \"query {
            user(login: \\\"%s\\\") {
              contributionsCollection(from: \\\"%s\\\") {
                contributionCalendar {
                  totalContributions
                }
              }
            }
          }\"
        }
        """.formatted(githubUsername, fromDateIso);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("your-github-access-token");
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

    // 로그인한 사용자 기준으로 커밋 수 업데이트 + 칭호 부여 호출
    public User updateCommitInfoAndGrantTitles(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow();

        int commitCount = getCommitCountSince(user.getGithubUsername(), user.getExpireAt());
        user.setPublicCommitCount(commitCount);
        userRepository.save(user);

        // 칭호 부여 서비스 호출 (한 방향 의존)
        titleCommitService.grantCommitTitleByCount(user);

        return user;
    }
}
