package com.example.gitmago.github;

import com.example.gitmago.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubPullRequestService {

    private static final String GITHUB_GRAPHQL_API = "https://api.github.com/graphql";

    public int getPullRequestCount(User user) {
        String token = user.getGithubAccessToken();
        String username = user.getGithubUsername();
        String fromDate = user.getExpireAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String graphqlQuery = """
            {
              search(query: "type:pr author:%s created:>=%s", type: ISSUE, first: 100) {
                issueCount
              }
            }
        """.formatted(username, fromDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("query", graphqlQuery);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.exchange(GITHUB_GRAPHQL_API, HttpMethod.POST, entity, Map.class);
            Map<?, ?> data = (Map<?, ?>) response.getBody().get("data");
            Map<?, ?> search = (Map<?, ?>) data.get("search");
            return (int) search.get("issueCount");
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 예외처리
        }
    }
}
