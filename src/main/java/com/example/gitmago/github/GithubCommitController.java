package com.example.gitmago.github;

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
            return ResponseEntity.badRequest().body(Map.of("error", "Missing or invalid Authorization header"));
        }

        String accessToken = authHeader.substring(7);
        int commitCount = githubCommitService.getPublicCommitCount(accessToken);
        return ResponseEntity.ok(Map.of("publicContributions", commitCount));
    }
}
