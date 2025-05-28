package com.example.gitmago.title;

import com.example.gitmago.user.UserRepository;
import org.springframework.stereotype.Service;
import com.example.gitmago.user.User;

import java.util.List;

@Service
public class TitleCommitService {

    private final UserRepository userRepository;

    public TitleCommitService(UserRepository userRepository) {
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

    public void grantCommitTitleByCount(User user) {
        int count = user.getPublicCommitCount();

        if (count >= 1000) grantTitleIfNotExists(user, "너는 찐 개발자다.", 5);
        else if (count >= 500) grantTitleIfNotExists(user, "개발자의 자질이 보인다", 4);
        else if (count >= 300) grantTitleIfNotExists(user, "300번째 커밋이네", 3);
        else if (count >= 100) grantTitleIfNotExists(user, "100번 찍어 안넘어가는 커밋없다", 2);
        else if (count >= 10) grantTitleIfNotExists(user, "이제 무르 익었네요.", 1);
    }


}
