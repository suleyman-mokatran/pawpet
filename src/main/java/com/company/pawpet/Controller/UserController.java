package com.company.pawpet.Controller;

import com.company.pawpet.Model.*;
import com.company.pawpet.notification.NotificationHandler;
import com.company.pawpet.PasswordUpdateRequest;
import com.company.pawpet.Repository.UserRepository;
import com.company.pawpet.Service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final NotificationHandler notificationHandler;

    // ✅ Inject NotificationHandler using Constructor
    public UserController(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @Autowired
    AppUserService appUserService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

   @Autowired
    PetService petService;

   @Autowired
    AppointmentService appointmentService;

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

    @PostMapping("/addpet/{id}/{categoryId}")
    public ResponseEntity<?> addNewPet(@PathVariable int id ,@PathVariable int categoryId, @RequestBody Pet pet, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        Pet savedPet = petService.addNewPet(pet,id,categoryId);
        return ResponseEntity.ok(savedPet);
    }

    @PutMapping("/updatepet/{id}/{categoryId}")
    public ResponseEntity<?> updatePet(@PathVariable int id,@PathVariable int categoryId, @RequestBody Pet pet, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Pet savedPet = petService.updatePet(id,categoryId,pet);
        return ResponseEntity.ok(savedPet);
    }
    @GetMapping("/getpet/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable int id) {
        return petService.getPetById(id).map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
    }

    @GetMapping("/getpetcategories")
    public List<Map<String,String>> findPetCategories(){
        return categoryService.findPetCategory();
    }

    @GetMapping("/getcategory/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
        Category category=  categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/getspecializations")
    public ResponseEntity<List<String>> getSpecializations() {
        List<String> specializations =doctorService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }

    @GetMapping("/getspecializeddoctors/{specialization}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<Doctor> doctors =doctorService.findDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/getappointments/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable int id){
        List<Appointment> appointmentList = appointmentService.findAppointmentsByDoctor(id);
        List<Appointment> filteredAppointments = new ArrayList<>();

        for(var appointment : appointmentList){
            if(!appointment.isBooked()){
                filteredAppointments.add(appointment);
            }
        }
        return ResponseEntity.ok(filteredAppointments);
    }

    @GetMapping("/getappointment/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable int id){
        Appointment appointment = appointmentService.getAppointment(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/getpetbycategory/{id}/{category}")
    public ResponseEntity<List<Pet>> getPetByCategory(@PathVariable int id, @PathVariable String category) {
        List<Pet> pets = petService.getAllPets(id);
        List<Pet> same = new ArrayList<>();
        for (var pet : pets) {
            Map<String, String> entry = pet.getPetCategory().getMSCategory();
            if (!entry.isEmpty()) {
                String firstKey = entry.keySet().iterator().next();
                if (firstKey.equals(category)) {
                    same.add(pet);
                }
            }
        }
            return ResponseEntity.ok(same);
    }

    @GetMapping("/getdoctor/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable int id){
        Doctor doctor = doctorService.findById(id).orElseThrow();
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/confirmbooking/{userId}/{doctorId}/{petId}/{appointmentId}")
    public ResponseEntity<String> confirmBooking(@PathVariable int userId,@PathVariable int doctorId,@PathVariable int petId,@PathVariable int appointmentId) throws IOException {
           Appointment appointment =  appointmentService.bookAppointment(userId,doctorId,petId,appointmentId);
        try {
            notificationHandler.sendNotificationToDoctor(doctorId, "New appointment booked!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Appointment booked successfully!");
    }

    @GetMapping("/getuserappointments/{id}")
    public ResponseEntity<List<Appointment>> getUserAppointments(@PathVariable int id){
        List<Appointment> appointmentList = appointmentService.findAppointmentsByUserId(id);
        return ResponseEntity.ok(appointmentList);
    }

    @PutMapping("/unbookappointment/{id}")
    public ResponseEntity<Appointment> unbookAppointment(@PathVariable int id) {
        return ResponseEntity.ok(appointmentService.unbookAppointment(id));
    }


}