package com.company.pawpet.Controller;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Optional<AppUser>> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve and return the user profile
        Optional<AppUser> profile = userRepository.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }
}