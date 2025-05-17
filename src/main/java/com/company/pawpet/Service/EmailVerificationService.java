package com.company.pawpet.Service;

import com.company.pawpet.Model.EmailVerification;
import com.company.pawpet.Repository.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailVerificationService {

    @Autowired
    private EmailVerificationRepository repository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCode(String email) {
        String code = generateCode();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);

        EmailVerification verification = repository.findByEmail(email)
                .orElse(new EmailVerification());

        verification.setEmail(email);
        verification.setCode(code);
        verification.setExpirationTime(expiration);
        repository.save(verification);

        sendEmail(email, code);
    }

    public boolean verifyCode(String email, String code) {
        Optional<EmailVerification> optional = repository.findByEmail(email);
        if (optional.isEmpty()) return false;

        EmailVerification verification = optional.get();
        return verification.getCode().equals(code) &&
                verification.getExpirationTime().isAfter(LocalDateTime.now());
    }

    private String generateCode() {
        Random random = new Random();
        int num = 100000 + random.nextInt(900000);
        return String.valueOf(num);
    }

    private void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Verification Code");
        message.setText("Your verification code is: " + code);
        mailSender.send(message);
    }
}

