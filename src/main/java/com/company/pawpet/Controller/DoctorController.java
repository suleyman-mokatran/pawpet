package com.company.pawpet.Controller;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Doctor;
import com.company.pawpet.PasswordUpdateRequest;
import com.company.pawpet.Repository.DoctorRepository;
import com.company.pawpet.Service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/doctor")
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    DoctorRepository doctorRepository;

    @GetMapping("/profile")
    public ResponseEntity<Doctor> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Doctor profile = doctorService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) {

        String oldPassword = passwordUpdateRequest.getOldPassword();
        String newPassword = passwordUpdateRequest.getNewPassword();
        String username = userDetails.getUsername();

        Doctor doctor = doctorService.findByUsername(username);

        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }

        if (!passwordEncoder.matches(oldPassword, doctor.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
        }

        doctor.setPassword(passwordEncoder.encode(newPassword));
        doctorRepository.save(doctor);

        return ResponseEntity.ok("Password updated successfully.");
    }
}
