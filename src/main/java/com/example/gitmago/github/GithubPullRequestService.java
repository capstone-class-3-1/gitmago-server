package com.example.gitmago.github;

import com.example.gitmago.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> body = new HashMap<>();
        body.put("query", graphqlQuery);

        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/json");

        var entity = new org.springframework.http.HttpEntity<>(body, headers);

        try {
            var response = restTemplate.postForEntity(GITHUB_GRAPHQL_API, entity, Map.class);
            Map<?, ?> data = (Map<?, ?>) response.getBody().get("data");
            Map<?, ?> search = (Map<?, ?>) data.get("search");
            return (int) search.get("issueCount");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
