package com.company.pawpet.Controller;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Doctor;
import com.company.pawpet.Model.Pet;
import com.company.pawpet.PasswordUpdateRequest;
import com.company.pawpet.Repository.UserRepository;
import com.company.pawpet.Service.AppUserService;
import com.company.pawpet.Service.PetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    AppUserService appUserService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

   @Autowired
    PetService petService;

    @GetMapping("/profile")
    public ResponseEntity<AppUser> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        AppUser profile = appUserService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) {

        String oldPassword = passwordUpdateRequest.getOldPassword();
        String newPassword = passwordUpdateRequest.getNewPassword();
        String username = userDetails.getUsername();

        AppUser user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully.");
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @Valid @RequestBody AppUser appUser, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        AppUser savedUser = appUserService.updateUser(id,appUser);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/getuser/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable int id) {
        return appUserService.getUserById(id).map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @GetMapping("/getpets/{id}")
    public ResponseEntity<List<Pet>> getAllPets(@PathVariable int id) {
        List<Pet> pets = petService.getAllPets(id);
        return ResponseEntity.ok(pets);}


    @DeleteMapping("/deletepet/{id}")
    public void deletePet(@PathVariable int id ){
        petService.deletePet(id);
    }

    @PostMapping("/addpet/{id}")
    public ResponseEntity<?> addNewPet(@PathVariable int id , @RequestBody Pet pet, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Pet savedPet = petService.addNewPet(pet,id);
        return ResponseEntity.ok(savedPet);
    }
}