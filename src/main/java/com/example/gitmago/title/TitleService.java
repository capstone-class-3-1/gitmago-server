package com.example.gitmago.title;

import com.example.gitmago.user.UserRepository;
import org.springframework.stereotype.Service;
import com.example.gitmago.user.User;

import java.util.List;

@Service
public class TitleService {

    private final UserRepository userRepository;

    public TitleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 획득한 칭호 조회
    public List<Title> getUserTitles(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow().getTitles();
    }

    // 칭호 착용
    public void equipTitle(String username, String titleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow();

        Title match = user.getTitles().stream()
                .filter(t -> t.getName().equals(titleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("칭호 없음"));

        user.setEquippedTitle(match);
        userRepository.save(user);
    }

    // 칭호 부여
    public void grantTitleIfNotExists(User user, String titleName, int level) {
        boolean alreadyHas = user.getTitles().stream()
                .anyMatch(t -> t.getName().equals(titleName));

        if (!alreadyHas) {
            user.getTitles().add(new Title(titleName, level));
            if (user.getEquippedTitle() == null) {
                user.setEquippedTitle(new Title(titleName, level));
            }
            userRepository.save(user);
        }
    }
}
