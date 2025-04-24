package com.example.gitmago.auth.controller;

import com.example.gitmago.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.registerUser(
                request.get("username"),
                request.get("password"),
                request.get("confirmPassword"),
                request.get("school"),
                request.get("email")
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.loginUser(
                request.get("username"),
                request.get("password")
        ));
    }

    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
