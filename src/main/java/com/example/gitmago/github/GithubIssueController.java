package com.example.gitmago.github;

import com.example.gitmago.user.User;
import com.example.gitmago.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/github/issues")
@RequiredArgsConstructor
public class GithubIssueController {

    private final GithubIssueService githubIssueService;
    private final UserRepository userRepository;

    @GetMapping("/{username}")
    public ResponseEntity<?> getIssueCount(@PathVariable String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }

        int issueCount = githubIssueService.getIssueCount(userOpt.get());
        if (issueCount == -1) {
            return ResponseEntity.status(500).body("이슈 조회 중 오류 발생");
        }

        return ResponseEntity.ok(Map.of("username", username, "issueCount", issueCount));
    }
}

