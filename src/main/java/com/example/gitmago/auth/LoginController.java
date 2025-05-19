package com.example.gitmago.auth;

import com.example.gitmago.jwt.JwtUtil;
import com.example.gitmago.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        return userRepository.findByUsername(username)
                .map(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        String token = jwtUtil.generateToken(username);
                        Map<String, String> response = new HashMap<>();
                        response.put("message", "로그인 성공");
                        response.put("token", token);
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.status(401).body(Map.of("message", "비밀번호가 불일치 합니다"));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("message", "사용자를 찾을 수 없습니다")));
    }
}
