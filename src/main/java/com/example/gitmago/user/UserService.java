package com.example.gitmago.user;

import com.example.gitmago.jwt.JwtUtil;
import com.example.gitmago.comno.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String registerUser(String username, String password, String confirmPassword, String school, String email) {
        if (!password.equals(confirmPassword)) {
            throw new ConflictException("비밀번호가 일치하지 않습니다.");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ConflictException("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("이미 등록된 이메일입니다.");
        }

        List<String> validSchools = List.of(
                "경북소프트웨어마이스터고", "대덕소프트웨어마이스터고",
                "대구소프트웨어마이스터고", "광주소프트웨어마이스터고", "부산소프트웨어마이스터고"
        );
        if (!validSchools.contains(school)) {
            throw new ConflictException("올바른 학교를 선택해주세요.");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .username(username)
                .password(hashedPassword)
                .school(school)
                .email(email)
                .emailVerified(false)
                .build();

        userRepository.save(user);
        return "회원가입 성공";
    }

    public String loginUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) return "아이디가 존재하지 않습니다.";

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) return "비밀번호가 틀렸습니다.";

        return jwtUtil.generateToken(username);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void verifyEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setEmailVerified(true);
            userRepository.save(user);
        });
    }

    public void setVerificationCode(String email, int code) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setVerificationCode(code);
            user.setExpireAt(LocalDateTime.now().plusMinutes(5));
            user.setEmailVerified(false);
            userRepository.save(user);
        });
    }

    public boolean checkVerificationCode(String email, int inputCode) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getVerificationCode() == inputCode)
                .map(user -> {
                    user.setEmailVerified(true);
                    userRepository.save(user);
                    return true;
                }).orElse(false);
    }
}
