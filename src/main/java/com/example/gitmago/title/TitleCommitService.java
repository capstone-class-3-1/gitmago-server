package com.example.gitmago.title;

import com.example.gitmago.github.GithubCommitService;
import com.example.gitmago.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.gitmago.user.User;

import java.time.LocalDateTime;
import java.util.List;


//칭호 부여만 담당 하고 커밋 갯수 조회는 GithubCommitService 에서 불러와야됨
@Service
@RequiredArgsConstructor
public class TitleCommitService {
    private final UserRepository userRepository;
    private final GithubCommitService githubCommitService;

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
    public void grantTitleIfNotExists(User user, String titleName, int level, TitleType type) {
        boolean alreadyHas = user.getTitles().stream()
                .anyMatch(t -> t.getName().equals(titleName));

        if (!alreadyHas) {
            Title title = new Title();
            title.setName(titleName);
            title.setLevel(level);
            title.setType(type);
            title.setObtained(true);
            title.setObtainedAt(LocalDateTime.now());
            title.setImageUrl("/images/title/" + type.name().toLowerCase() + "_" + level + ".png");

            user.getTitles().add(title);

            if (user.getEquippedTitle() == null) {
                user.setEquippedTitle(title);
            }
            userRepository.save(user);
        }
    }

    public void grantCommitTitleByCount(User user) {
        int count = githubCommitService.getCommitCountSince(user.getGithubUsername(), user.getExpireAt());
        user.setPublicCommitCount(count);

        if (count >= 1000) grantTitleIfNotExists(user, "찐 개발자.", 5, TitleType.COMMIT);
        else if (count >= 500) grantTitleIfNotExists(user, "개발자의 자질이 보인다", 4, TitleType.COMMIT);
        else if (count >= 300) grantTitleIfNotExists(user, "300번째 커밋이네", 3,TitleType.COMMIT);
        else if (count >= 100) grantTitleIfNotExists(user, "100번 찍어 안넘어가는 커밋없다", 2,TitleType.COMMIT);
        else if (count >= 10) grantTitleIfNotExists(user, "이제 무르 익었네요.", 1,TitleType.COMMIT);

        userRepository.save(user); //레포지토리에 저장. DB 반영
    }


}
