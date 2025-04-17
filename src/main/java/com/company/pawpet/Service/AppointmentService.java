package com.company.pawpet.Service;


import com.company.pawpet.Model.*;
import com.company.pawpet.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    UserRepository appUserRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    ServiceRepository serviceRepository;

    public Appointment addNewDoctorAppointment(int id , Appointment appointment){

        Appointment savedAppointment = new Appointment();

        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isPresent()) {
            Doctor savedDoctor = doctorOptional.get();
            savedAppointment.setDoctor(savedDoctor);
        } else {
            throw new EntityNotFoundException("Doctor with ID " + id + " not found");
        }

        savedAppointment.setPrice(appointment.getPrice());
        savedAppointment.setBooked(appointment.isBooked());
        savedAppointment.setEndTime(appointment.getEndTime());
        savedAppointment.setStartTime(appointment.getStartTime());
        savedAppointment.setSelectedDate(appointment.getSelectedDate());
        savedAppointment.setStatus(null);
        savedAppointment.setType("Vet");

        return appointmentRepository.save(savedAppointment);
    }

    public Appointment addNewServiceAppointment(int id , Appointment appointment){

        Appointment savedAppointment = new Appointment();

        ServiceModel service = serviceRepository.findById(id).orElseThrow();

        savedAppointment.setService(service);
        savedAppointment.setPrice(appointment.getPrice());
        savedAppointment.setBooked(appointment.isBooked());
        savedAppointment.setEndTime(appointment.getEndTime());
        savedAppointment.setStartTime(appointment.getStartTime());
        savedAppointment.setSelectedDate(appointment.getSelectedDate());
        savedAppointment.setStatus(null);
        savedAppointment.setType("Service");

        return appointmentRepository.save(savedAppointment);
    }

    public Appointment getAppointment(int appointmentId){
        return appointmentRepository.findById(appointmentId).orElseThrow();
    }

    public Appointment updateDoctorAppointment(int id , Appointment appointment){
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);

        if (existingAppointment.isEmpty()) {
            throw new RuntimeException("Appointment with ID " + id + " not found.");
        }

        Appointment appointmentToUpdate = existingAppointment.get();

        appointmentToUpdate.setPrice(appointment.getPrice());
        appointmentToUpdate.setBooked(appointment.isBooked());
        appointmentToUpdate.setStartTime(appointment.getStartTime());
        appointmentToUpdate.setEndTime(appointment.getEndTime());
        appointmentToUpdate.setSelectedDate(appointment.getSelectedDate());
        appointmentToUpdate.setStatus(appointment.getStatus());


        return appointmentRepository.save(appointmentToUpdate);
    }

    public Appointment updateServiceAppointment(int id , Appointment appointment){
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);

        if (existingAppointment.isEmpty()) {
            throw new RuntimeException("Appointment with ID " + id + " not found.");
        }

        Appointment appointmentToUpdate = existingAppointment.get();

        appointmentToUpdate.setPrice(appointment.getPrice());
        appointmentToUpdate.setBooked(appointment.isBooked());
        appointmentToUpdate.setStartTime(appointment.getStartTime());
        appointmentToUpdate.setEndTime(appointment.getEndTime());
        appointmentToUpdate.setSelectedDate(appointment.getSelectedDate());
        appointmentToUpdate.setStatus(appointment.getStatus());


        return appointmentRepository.save(appointmentToUpdate);
    }

    public List<Appointment> getAllAppointments(){
        return appointmentRepository.findAll();
    }

    public void DeleteAppointment(int appointmentId){
        appointmentRepository.deleteById(appointmentId);
    }

    public List<Appointment> findAppointmentsByDoctor(int doctorId){
        return appointmentRepository.findAppointmentsByDoctor(doctorId);
    }

    public List<Appointment> findAppointmentsByService(int serviceId){
        return appointmentRepository.findAppointmentsByService(serviceId);
    }

    public List<Appointment> findAppointmentsByPrice(float price){
        return appointmentRepository.findAppointmentsByPrice(price);
    }

    public List<Appointment> findAppointmentsByStatus(String status){
        return appointmentRepository.findAppointmentsByStatus(status);
    }

    public List<Appointment> findAppointmentsByDoctorId(int id){
        return appointmentRepository.findAppointmentsByDoctor(id);
    }

    public List<Appointment> findAppointmentsByUserId(int id){
        return appointmentRepository.findAppointmentsByUser(id);
    }

    public String getWorkingTimeByDay(String day,int id){
        return doctorRepository.findByDayAndDoctorId(day,id);
    }

    public String getSPWorkingTimeByDay(String day,int id){
        return serviceProviderRepository.findByDayAndSpId(day,id);
    }


    public Appointment bookAppointment(int userId,int doctorId, int petId, int appointmentId){
        Appointment existingAppointment = appointmentRepository.findById(appointmentId).orElseThrow();
        AppUser existingUser = appUserRepository.findById(userId).orElseThrow();
        Doctor existingDoctor = doctorRepository.findById(doctorId).orElseThrow();
        Pet existingPet = petRepository.findById(petId).orElseThrow();

        Appointment updatedAppointment = existingAppointment;
        updatedAppointment.setAppUser(existingUser);
        updatedAppointment.setDoctor(existingDoctor);
        updatedAppointment.setPet(existingPet);
        updatedAppointment.setBooked(true);

       return appointmentRepository.save(updatedAppointment);
    }

    public Appointment unbookAppointment(int id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();

        appointment.setBooked(false);
        appointment.setAppUser(null);
        appointment.setPet(null);

       return appointmentRepository.save(appointment); // Save changes
    }

    public void rescheduleBookedAppointment(int oldId,int newId){
        Appointment oldScheduledAppointment = appointmentRepository.findById(oldId).orElseThrow();

        Appointment newScheduledAppointment = appointmentRepository.findById(newId).orElseThrow();

        newScheduledAppointment.setAppUser(oldScheduledAppointment.getAppUser());
        newScheduledAppointment.setPet(oldScheduledAppointment.getPet());

        oldScheduledAppointment.setBooked(false);
        oldScheduledAppointment.setAppUser(null);
        oldScheduledAppointment.setPet(null);
        newScheduledAppointment.setBooked(true);

        appointmentRepository.save(newScheduledAppointment);
        appointmentRepository.save(oldScheduledAppointment);

    }

    public Appointment bookServiceAppointment(int userId,int serviceId, int petId, int appointmentId){
        Appointment existingAppointment = appointmentRepository.findById(appointmentId).orElseThrow();
        AppUser existingUser = appUserRepository.findById(userId).orElseThrow();
        ServiceModel existingService = serviceRepository.findById(serviceId).orElseThrow();
        Pet existingPet = petRepository.findById(petId).orElseThrow();

        Appointment updatedAppointment = existingAppointment;
        updatedAppointment.setAppUser(existingUser);
        updatedAppointment.setService(existingService);
        updatedAppointment.setPet(existingPet);
        updatedAppointment.setBooked(true);

        return appointmentRepository.save(updatedAppointment);
    }

}

