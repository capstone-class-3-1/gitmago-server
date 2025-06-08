package com.example.gitmago.github;

import com.example.gitmago.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubCommitController {

    private final GithubCommitService githubCommitService;

    @GetMapping("/commit-count")
    public ResponseEntity<?> getCommitCount(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "헤더가에 에세스토큰 값이 없음"));
        }

        String accessToken = authHeader.substring(7);
        User user = githubCommitService.updateCommitInfoAndGrantTitles(accessToken);
        int commitCount = user.getPublicCommitCount();

        return ResponseEntity.ok(Map.of("publicContributions", commitCount));
    }
}
