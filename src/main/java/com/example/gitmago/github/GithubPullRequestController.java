package com.example.gitmago.github;

import com.example.gitmago.user.User;
import com.example.gitmago.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/github/pullrequests")
@RequiredArgsConstructor
public class GithubPullRequestController {

    private final UserRepository userRepository;
    private final GithubPullRequestService pullRequestService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getPullRequestCount(@PathVariable String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }

        User user = userOpt.get();
        int prCount = pullRequestService.getPullRequestCount(user);
        if (prCount == -1) {
            return ResponseEntity.status(500).body("GitHub PR 조회 중 오류가 발생했습니다.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("pullRequestCount", prCount);

        return ResponseEntity.ok(response);
    }
}
