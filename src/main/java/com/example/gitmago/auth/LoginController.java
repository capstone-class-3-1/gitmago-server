package com.example.gitmago.auth;

import com.example.gitmago.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<String> checkPasswordMatch(@RequestBody Map<String, String>request){
        String username = request.get("username");
        String password = request.get("password");

        return userRepository.findByUsername(username)
                .map(user->{
                    if(passwordEncoder.matches(password,user.getPassword())) {
                        return ResponseEntity.ok("비밀번호가 일치 합니다");
                    }else{
                        return ResponseEntity.status(401).body("비밀번호가 불일치 합니다");
                    }
                })
                .orElseGet(()-> ResponseEntity.status(404).body("사용자를 찾을수 없습니다"));
    }
}
