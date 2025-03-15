package com.company.pawpet.Controller;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.ProductProvider;
import com.company.pawpet.PasswordUpdateRequest;
import com.company.pawpet.Repository.ProductProviderRepository;
import com.company.pawpet.Service.ProductProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pp")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('PP')")
public class PPController {

    @Autowired
    ProductProviderService productProviderService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ProductProviderRepository productProviderRepository;

    @GetMapping("/profile")
    public ResponseEntity<ProductProvider> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProductProvider profile = productProviderService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) {

        String oldPassword = passwordUpdateRequest.getOldPassword();
        String newPassword = passwordUpdateRequest.getNewPassword();
        String username = userDetails.getUsername();

        ProductProvider pp = productProviderRepository.findByUsername(username);

        if (pp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PP not found.");
        }

        if (!passwordEncoder.matches(oldPassword, pp.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
        }

        pp.setPassword(passwordEncoder.encode(newPassword));
        productProviderRepository.save(pp);

        return ResponseEntity.ok("Password updated successfully.");
    }
}
