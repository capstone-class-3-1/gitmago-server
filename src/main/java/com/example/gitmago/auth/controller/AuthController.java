package com.example.gitmago.auth.controller;

import com.example.gitmago.auth.exception.ConflictException;
import com.example.gitmago.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        try {
            String message = authService.registerUser(
                    request.get("username"),
                    request.get("password"),
                    request.get("confirmPassword"),
                    request.get("school"),
                    request.get("email")
            );
            return ResponseEntity.ok(message);
        } catch (ConflictException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<String> checkUsername(@RequestParam String username) {
        boolean exists = authService.isUsernameTaken(username);
        if (exists) {
            return ResponseEntity.status(409).body("이미 사용 중인 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        boolean exists = authService.isEmailTaken(email);
        if (exists) {
            return ResponseEntity.status(409).body("이미 등록된 이메일입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 이메일입니다.");
        }
    }
}
