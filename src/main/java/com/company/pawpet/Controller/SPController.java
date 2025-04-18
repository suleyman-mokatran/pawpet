package com.company.pawpet.Controller;

import com.company.pawpet.Model.*;
import com.company.pawpet.PasswordUpdateRequest;
import com.company.pawpet.Repository.DoctorRepository;
import com.company.pawpet.Repository.ServiceProviderRepository;
import com.company.pawpet.Service.*;
import com.company.pawpet.notification.NotificationHandler;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/sp")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('SP')")
public class SPController {

    private final NotificationHandler notificationHandler;

    public SPController(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }


    @Autowired
    ServiceProviderService serviceProviderService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    CompanyService companyService;

    @Autowired
    ServiceService serviceService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    PetService petService;

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

    @PutMapping("/updatesp/{id}/{companyId}")
    public ResponseEntity<?> updateSp(@PathVariable int id, @PathVariable int companyId, @Valid @RequestBody ServiceProvider sp, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        ServiceProvider savedSp = serviceProviderService.updateServiceProvider(id, companyId, sp);
        return ResponseEntity.ok(savedSp);
    }

    @GetMapping("/getallcompanies")
    public List<Company> getAllCompanies() {
        return companyService.findAllCompanies();
    }

    @PutMapping("/setavailability/{spid}")
    public ResponseEntity<String> setAvailability(@PathVariable int spid,@RequestBody Map<String,String> availability){
         serviceProviderService.setAvailability(spid,availability);
         return ResponseEntity.ok("success");
    }

    @GetMapping("/getavailability/{spid}")
    public ResponseEntity<Map<String,String>> getAvailability(@PathVariable int spid){
        return ResponseEntity.ok(serviceProviderService.getAvailability(spid));
    }
    @GetMapping("/getsp/{id}")
    public ResponseEntity<ServiceProvider> getSpById(@PathVariable int id) {

        ServiceProvider sp = serviceProviderService.getSPById(id);
        return ResponseEntity.ok(sp);
    }

    @GetMapping("/getservices/{id}")
    public ResponseEntity<List<ServiceModel>> getAllServices(@PathVariable int id) {
        return ResponseEntity.ok(serviceService.getAllServicesBySP(id));
    }

    @GetMapping("/getservicecategories")
    public List<Map<String, String>> findServiceCategories() {
        return categoryService.findServiceCategory();
    }

    @PostMapping("/addservice/{categoryId}/{spId}")
    public ResponseEntity<ServiceModel> addNewService(@PathVariable int categoryId, @PathVariable int spId, @RequestBody ServiceModel serviceModel) {
        return ResponseEntity.ok(serviceService.addNewService(categoryId, spId, serviceModel));
    }

    @DeleteMapping("/deleteservice/{serviceid}")
    public void deleteservice(@PathVariable int serviceid){
        serviceService.deleteService(serviceid);
    }

    @GetMapping("/getservice/{serviceid}")
    public ResponseEntity<ServiceModel> getServiceById(@PathVariable int serviceid){
        return ResponseEntity.ok(serviceService.getServiceById(serviceid));
    }

    @PutMapping("/updateservice/{categoryId}/{serviceId}")
    public ResponseEntity<ServiceModel> updateService(@PathVariable int categoryId, @PathVariable int serviceId, @RequestBody ServiceModel service) {
        return ResponseEntity.ok(serviceService.updateService(categoryId, serviceId, service));
    }

    @PostMapping("/addappointment/{id}")
    public ResponseEntity<?> addNewAppointment(@PathVariable int id,@RequestBody Appointment appointment, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Appointment newAppointment = appointmentService.addNewServiceAppointment(id,appointment);
        return ResponseEntity.ok(newAppointment);
    }

    @GetMapping("/getappointments/{id}")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable int id){
        List<Appointment> appointments = appointmentService.findAppointmentsByService(id);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/updateappointment/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable int id,@RequestBody Appointment appointment){
        Appointment savedAppointment =  appointmentService.updateServiceAppointment(id,appointment);
        return ResponseEntity.ok(savedAppointment);
    }

    @DeleteMapping("/deleteappointment/{id}")
    public void deleteAppointment(@PathVariable int id){
        appointmentService.DeleteAppointment(id);
    }

    @GetMapping("/getworkingtimebyday/{day}/{id}")
    public ResponseEntity<String> getWorkingTime(@PathVariable String day, @PathVariable int id){
        String time =  appointmentService.getSPWorkingTimeByDay(day,id);
        return ResponseEntity.ok(time);
    }

    @GetMapping("/getappointmentbyid/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable int id){
        return ResponseEntity.ok(appointmentService.getAppointment(id));
    }

    @PutMapping("/rescheduleappointment/{oldId}/{newId}/{userId}")
    public ResponseEntity<String> rescheduleAppointment(@PathVariable int oldId,
                                                        @PathVariable int newId,
                                                        @PathVariable int userId) {
        try {
            appointmentService.rescheduleBookedAppointment(oldId, newId);

            notificationHandler.sendNotificationToUser(userId, "Your appointment has rescheduled!");


            return ResponseEntity.ok("Appointment rescheduled successfully!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found!");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending notification: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error rescheduling appointment: " + e.getMessage());
        }
    }

    @PutMapping("/cancelappointment/{id}")
    public ResponseEntity<Appointment> cancelBooking(@PathVariable int id){
        Appointment appointment = appointmentService.unbookAppointment(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/getpet/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable int id) {
        return petService.getPetById(id).map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
    }

    @PutMapping("/updatemissedappointment/{id}")
    public ResponseEntity<Appointment> updateMissedAppointment(@PathVariable int id){
        Appointment appointment = appointmentService.getAppointment(id);
        appointment.setStatus("missed");
        return ResponseEntity.ok(appointmentService.updateServiceAppointment(id,appointment));
    }

    @PutMapping("/updateattendedappointment/{id}")
    public ResponseEntity<Appointment> updateAttendedAppointment(@PathVariable int id){
        Appointment appointment = appointmentService.getAppointment(id);
        appointment.setStatus("done");
        return ResponseEntity.ok(appointmentService.updateServiceAppointment(id,appointment));
    }
}
