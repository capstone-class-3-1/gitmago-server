package com.example.gitmago.auth.service;

import com.example.gitmago.auth.model.User;
import com.example.gitmago.auth.repository.UserRepository;
import com.example.gitmago.auth.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "비밀번호가 일치하지 않습니다.";
        }
        if (userRepository.findByUsername(username).isPresent()) {
            return "이미 사용 중인 아이디입니다.";
        }
        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(new User(username, hashedPassword));
        return "회원가입 성공";
    }

    public String loginUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "아이디가 존재하지 않습니다.";
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "비밀번호가 틀렸습니다.";
        }
        return jwtUtil.generateToken(username);
    }
}
