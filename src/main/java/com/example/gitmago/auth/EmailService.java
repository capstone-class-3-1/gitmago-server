package com.example.gitmago.auth;

import com.example.gitmago.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserService authService;

    private static final String senderEmail = "gitmago59@gmail.com";

    public int generateCode() {
        return new Random().nextInt(900000) + 100000;
    }

    public MimeMessage createMail(String mail, int code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("이메일 인증");
        String body = "<h3>요청하신 인증 번호입니다.</h3><h1>" + code + "</h1><h3>감사합니다.</h3>";
        message.setText(body, "UTF-8", "html");
        return message;
    }

    public void sendMail(String mail) throws MessagingException {
        int code = generateCode();
        authService.setVerificationCode(mail, code);
        javaMailSender.send(createMail(mail, code));
    }

    public boolean verifyCode(String email, int inputCode) {
        return authService.checkVerificationCode(email, inputCode);
    }
}
