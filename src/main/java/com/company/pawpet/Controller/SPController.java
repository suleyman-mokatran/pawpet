package com.company.pawpet.Controller;

import com.company.pawpet.Model.Doctor;
import com.company.pawpet.Model.ServiceProvider;
import com.company.pawpet.PasswordUpdateRequest;
import com.company.pawpet.Repository.DoctorRepository;
import com.company.pawpet.Repository.ServiceProviderRepository;
import com.company.pawpet.Service.DoctorService;
import com.company.pawpet.Service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sp")
@PreAuthorize("hasRole('SP')")
public class SPController {

    @Autowired
    ServiceProviderService serviceProviderService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @GetMapping("/profile")
    public ResponseEntity<ServiceProvider> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ServiceProvider profile = serviceProviderService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) {

        String oldPassword = passwordUpdateRequest.getOldPassword();
        String newPassword = passwordUpdateRequest.getNewPassword();
        String username = userDetails.getUsername();

        ServiceProvider sp = serviceProviderService.findByUsername(username);

        if (sp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SP not found.");
        }

        if (!passwordEncoder.matches(oldPassword, sp.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
        }

        sp.setPassword(passwordEncoder.encode(newPassword));
        serviceProviderRepository.save(sp);

        return ResponseEntity.ok("Password updated successfully.");
    }
}
