package com.example.gitmago.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "gitmago59@gmail.com";

    private final Map<String, Integer> verificationCodes = new HashMap<>();

    public int generateCode() {
        return new Random().nextInt(900000) + 100000;
    }

    public MimeMessage createMail(String mail) throws MessagingException {
        int code = generateCode();
        verificationCodes.put(mail, code);

        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("이메일 인증");

        String body = "<h3>요청하신 인증 번호입니다.</h3>"
                + "<h1>" + code + "</h1>"
                + "<h3>감사합니다.</h3>";

        message.setText(body, "UTF-8", "html");

        return message;
    }

    public int sendMail(String mail) {
        try {
            MimeMessage message = createMail(mail);
            javaMailSender.send(message);
            return verificationCodes.get(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean verifyCode(String mail, int inputCode) {
        return verificationCodes.containsKey(mail) && verificationCodes.get(mail) == inputCode;
    }
}
