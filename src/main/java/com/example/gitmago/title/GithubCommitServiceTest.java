package com.example.gitmago.title;

import com.example.gitmago.github.GithubCommitService;
import com.example.gitmago.user.User;
import com.example.gitmago.user.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class GithubCommitServiceTest {

    @Autowired
    private GithubCommitService githubCommitService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCommitCountAndTitleGranting() {

        User user = new User();
        user.setUsername("testuser");
        user.setGithubUsername("realGithubId");
        userRepository.save(user);


        String fakeToken = "유효한 JWT 액세스 토큰";
        githubCommitService.updateCommitInfoAndGrantTitles(fakeToken);


        User updated = userRepository.findByUsername("testuser").orElseThrow();
        System.out.println("🏅 부여된 칭호 목록: ");
        updated.getTitles().forEach(t -> System.out.println(t.getName() + " (" + t.getLevel() + ")"));

        assertThat(updated.getTitles().size()).isGreaterThan(0);
        assertThat(updated.getPublicCommitCount()).isGreaterThan(0);
    }
}

