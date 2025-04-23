package com.example.gitmago.email.service;

import com.example.gitmago.auth.model.Auth;
import com.example.gitmago.auth.repository.AuthRepository;
import com.example.gitmago.email.model.Email;
import com.example.gitmago.email.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final AuthRepository userRepository;

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
        Email verification = Email.builder()
                .email(mail)
                .code(code)
                .verified(false)
                .expireAt(LocalDateTime.now().plusMinutes(5))
                .build();

        emailVerificationRepository.save(verification);
        MimeMessage message = createMail(mail, code);
        javaMailSender.send(message);
    }

    public boolean verifyCode(String email, int inputCode) {
        Optional<Email> optional = emailVerificationRepository.findTopByEmailOrderByExpireAtDesc(email);
        if (optional.isPresent()) {
            Email ev = optional.get();
            if (ev.getCode() == inputCode) {
                ev.setVerified(true);
                emailVerificationRepository.save(ev);


                Optional<Auth> userOptional = userRepository.findByEmail(email);
                userOptional.ifPresent(user -> {
                    user.setEmailVerified(true);
                    userRepository.save(user);
                });

                return true;
            }
        }
        return false;
    }
}
