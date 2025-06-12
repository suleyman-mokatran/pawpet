package com.company.pawpet.Controller;

import com.company.pawpet.Repository.UserRepository;
import com.company.pawpet.Service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")

public class EmailVerificationController {

    @Autowired
    UserRepository appUserRepository;

    @Autowired
    private EmailVerificationService service;

    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestParam String email) {
        if (appUserRepository.findByUsername(email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        }

        service.sendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");
    }



    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email,
                                             @RequestParam String code) {
        boolean success = service.verifyCode(email, code);
        if (success) {
            return ResponseEntity.ok("Email verified");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired code");
        }
    }
}

