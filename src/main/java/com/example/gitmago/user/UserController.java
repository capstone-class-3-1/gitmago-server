package com.example.gitmago.user;

import com.example.gitmago.comno.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService authService;

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
}
