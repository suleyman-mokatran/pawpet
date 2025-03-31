package com.company.pawpet.Controller;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Appointment;
import com.company.pawpet.Model.Doctor;
import com.company.pawpet.Model.Pet;
import com.company.pawpet.PasswordUpdateRequest;
import com.company.pawpet.Repository.AppointmentRepository;
import com.company.pawpet.Repository.DoctorRepository;
import com.company.pawpet.Service.AppUserService;
import com.company.pawpet.Service.AppointmentService;
import com.company.pawpet.Service.DoctorService;
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
@RequestMapping("/doctor")
@PreAuthorize("hasRole('DOCTOR')")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AppUserService appUserService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    PetService petService;

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

    @PutMapping("/updatedoctor/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable int id, @Valid @RequestBody Doctor doctor, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Doctor savedDoctor = doctorService.updateDoctor(id,doctor);
        return ResponseEntity.ok(savedDoctor);
    }

    @GetMapping("/getdoctor/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable int id) {
        return doctorService.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
    }

    @PostMapping("/addappointment/{id}")
    public ResponseEntity<?> addNewAppointment(@PathVariable int id,@RequestBody Appointment appointment, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Appointment newAppointment = appointmentService.addNewAppointment(id,appointment);
        return ResponseEntity.ok(newAppointment);
    }

    @GetMapping("/getappointments/{id}")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable int id){
        List<Appointment> appointments = appointmentService.findAppointmentsByDoctorId(id);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/getworkingtimebyday/{day}/{id}")
    public ResponseEntity<String> getWorkingTime(@PathVariable String day, @PathVariable int id){
        String time =  appointmentService.getWorkingTimeByDay(day,id);
        return ResponseEntity.ok(time);
    }

    @PutMapping("/updateappointment/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable int id,@RequestBody Appointment appointment){
        Appointment savedAppointment =  appointmentService.updateAppointment(id,appointment);
        return ResponseEntity.ok(savedAppointment);
    }

    @DeleteMapping("/deleteappointment/{id}")
    public void deleteAppointment(@PathVariable int id){
         appointmentService.DeleteAppointment(id);
    }

    @GetMapping("/getappointment/{id}")
    public ResponseEntity<List<?>> getAppointmentById(@PathVariable int id){
        List<?>  appointment = appointmentRepository.findAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/getappointmentbyid/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable int id){
        return ResponseEntity.ok(appointmentService.getAppointment(id));
    }

    @PutMapping("/cancelappointment/{id}")
    public ResponseEntity<Appointment> cancelBooking(@PathVariable int id){
    Appointment appointment = appointmentService.unbookAppointment(id);
    return ResponseEntity.ok(appointment);
    }

    @GetMapping("/getpet/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable int id){
        Pet pet = petService.getPetById(id).orElseThrow();
        return ResponseEntity.ok(pet);
    }

}
