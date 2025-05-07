package com.example.gitmago.github;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/github")
@RequiredArgsConstructor
public class GithubController {
    private final GithubService githubService;

    @GetMapping("/callback")
    public ResponseEntity<String> githubCallback(@RequestParam String code, HttpServletResponse response) {
        try{
            String result = githubService.processGithubOAuth(code);
            return ResponseEntity.ok(result);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body("GitHub 인증실패 :" +e.getMessage());
        }
    }
}
