package com.example.gitmago.github;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubAuthController {
    @GetMapping("/auth/github/callback")
    public String githubCallback(@RequestParam("code") String code){
        System.out.println("받은 Github code" + code);
        return "Github 로그인 성공" + code;
    }
}
