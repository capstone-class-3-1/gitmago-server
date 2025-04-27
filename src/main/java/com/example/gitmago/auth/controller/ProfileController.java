package com.example.gitmago.auth.controller;

import com.example.gitmago.auth.model.User;
import com.example.gitmago.auth.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
            String username = userDetails.getUsername();
            User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("user not found"));

            Map<String,Object> profile = new HashMap<>();
            profile.put("username", user.getUsername());
            profile.put("email", user.getEmail());
            profile.put("school", user.getSchool());
            profile.put("githubLinked", user.getGithubId() != null);
            profile.put("githubUsername", user.getGithubUsername());
            profile.put("githubAvatar", user.getGithubAvatar());
            profile.put("githubEmail", user.getGithubEmail());

            return ResponseEntity.ok(profile);


    }
}
