package com.example.gitmago.title;

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
}

