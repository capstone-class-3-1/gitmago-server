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
        String username = request.get("username");
        String password = request.get("password");
        String confirmPassword = request.get("confirmPassword");
        String school = request.get("school");
        return ResponseEntity.ok(authService.registerUser(username, password, confirmPassword, school));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String token = authService.loginUser(username, password);
        return ResponseEntity.ok(token);
    }
}
