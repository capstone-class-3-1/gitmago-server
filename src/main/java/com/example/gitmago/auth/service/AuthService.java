package com.example.gitmago.auth.service;

import com.example.gitmago.auth.model.Auth;
import com.example.gitmago.auth.repository.AuthRepository;
import com.example.gitmago.auth.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    private final AuthRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AuthRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(String username, String password, String confirmPassword, String school) {
        if (!password.equals(confirmPassword)) {
            return "비밀번호가 일치하지 않습니다.";
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return "이미 사용 중인 아이디입니다.";
        }

        List<String> validSchools = List.of(
                "경북소프트웨어마이스터고",
                "대덕소프트웨어마이스터고",
                "대구소프트웨어마이스터고",
                "광주소프트웨어마이스터고",
                "부산소프트웨어마이스터고"
        );

        if (!validSchools.contains(school)) {
            return "올바른 학교를 선택해주세요.";
        }

        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(new Auth(username, hashedPassword, school));
        return "회원가입 성공";
    }

    public String loginUser(String username, String password) {
        Optional<Auth> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "아이디가 존재하지 않습니다.";
        }

        Auth user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "비밀번호가 틀렸습니다.";
        }

        return jwtUtil.generateToken(username);
    }
}
