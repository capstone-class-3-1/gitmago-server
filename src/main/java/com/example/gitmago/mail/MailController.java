package com.example.gitmago.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> mailSend(@RequestParam String mail) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            int code = emailService.sendMail(mail);
            map.put("success", true);
            map.put("message", "인증번호가 전송되었습니다.");
        } catch (Exception e) {
            map.put("success", false);
            map.put("error", e.getMessage());
        }

        return ResponseEntity.ok(map);
    }

    @GetMapping("/check")
    public ResponseEntity<?> mailCheck(@RequestParam String mail, @RequestParam String userNumber) {
        boolean isMatch = emailService.verifyCode(mail, Integer.parseInt(userNumber));
        return ResponseEntity.ok(isMatch);
    }
}
