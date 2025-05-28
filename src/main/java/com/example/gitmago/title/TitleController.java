package com.example.gitmago.title;

import com.example.gitmago.user.User;
import com.example.gitmago.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/title")
@RequiredArgsConstructor
public class TitleController {

    private final TitleCommitService titleService;
    private final UserRepository userRepository;

    @GetMapping("/my")
    public ResponseEntity<?> getMyTitles(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(titleService.getUserTitles(userDetails.getUsername()));
    }

    @PostMapping("/equip")
    public ResponseEntity<?> equip(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestBody Map<String, String> body) {
        titleService.equipTitle(userDetails.getUsername(), body.get("name"));
        return ResponseEntity.ok("칭호 착용 완료");
    }

    @PostMapping("/grant")
    public ResponseEntity<?> grantTitle(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TitleDTO request
    ) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        titleService.grantTitleIfNotExists(user, request.getTitleName(), request.getLevel(), request.getType());
        return ResponseEntity.ok(Map.of("message", "칭호 부여 완료"));
    }

}

