package com.company.pawpet.Service;


import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Appointment;
import com.company.pawpet.Model.Doctor;
import com.company.pawpet.Model.ServiceProvider;
import com.company.pawpet.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    AppointmentService appointmentService;

    final BCryptPasswordEncoder passwordEncoder;

    public DoctorService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Doctor addNewDoctor(Doctor doctor){

        Doctor newDoctor =new Doctor();

        newDoctor.setUsername(doctor.getUsername());
        newDoctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        newDoctor.setRole(Role.DOCTOR);
        newDoctor.setAddress(doctor.getAddress());
        newDoctor.setFirstname(doctor.getFirstname());
        newDoctor.setLastname(doctor.getLastname());
        newDoctor.setPhone(doctor.getPhone());
        newDoctor.setGender(doctor.getGender());
        newDoctor.setBirthDate(doctor.getBirthDate());
        newDoctor.setImage(doctor.getImage());
        newDoctor.setAvailableDays(doctor.getAvailableDays());
        newDoctor.setSpecialization(doctor.getSpecialization());
        newDoctor.setExperienceYears(doctor.getExperienceYears());

        return doctorRepository.save(newDoctor);
    }

    public Doctor updateDoctor(int doctorId, Doctor doctor) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(doctorId);

        if (existingDoctor.isEmpty()) {
            throw new RuntimeException("Doctor with ID " + doctorId + " not found.");
        }

        Doctor doctorToUpdate = existingDoctor.get();

        doctorToUpdate.setFirstname(doctor.getFirstname());
        doctorToUpdate.setLastname(doctor.getLastname());
        doctorToUpdate.setBirthDate(doctor.getBirthDate());
        doctorToUpdate.setGender(doctor.getGender());
        doctorToUpdate.setPhone(doctor.getPhone());
        doctorToUpdate.setAddress(doctor.getAddress());
        doctorToUpdate.setSpecialization(doctor.getSpecialization());
        doctorToUpdate.setAvailableDays(doctor.getAvailableDays());
        doctorToUpdate.setExperienceYears(doctor.getExperienceYears());
        doctorToUpdate.setImage(doctor.getImage());
        doctorToUpdate.setUrgent(doctor.getUrgent());

        return doctorRepository.save(doctorToUpdate);
    }

    public void deleteDoctor(int doctorId){
        doctorRepository.deleteById(doctorId);
    }

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    public Optional<Doctor> findById(int doctorId){
        return doctorRepository.findById(doctorId);
    }

    public List<Doctor> findDoctorsBySpecialization(String specialization){

        return doctorRepository.findDoctorBySpecialization(specialization);
    }

    public Doctor findByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }

    public List<String> getAllSpecializations(){
        return doctorRepository.findSpecializations();
    }

    public String getAvailableDay(String day,int doctorId){
        String available = doctorRepository.findByDayAndDoctorId(day,doctorId);
        if(available.isEmpty() || available==null){
            return "This Day is not working day";
        }
        else{
            return available;
        }
    }

    public void setAvailability(int doctorId, Map<String, String> newAvailability) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        Map<String, String> oldAvailability = doctor.getAvailableDays();
        List<Appointment> appointments = appointmentService.findAppointmentsByDoctorId(doctorId);
        for (Map.Entry<String, String> entry : oldAvailability.entrySet()) {
            String day = entry.getKey();
            String oldTimeRange = entry.getValue();
            String newTimeRange = newAvailability.get(day);
            if (newTimeRange != null && !newTimeRange.trim().replaceAll("\\s+", "").equalsIgnoreCase(oldTimeRange.trim().replaceAll("\\s+", ""))) {
                for (Appointment appointment : appointments) {
                    if (appointment.getDayOfWeek().toString().equalsIgnoreCase(day)) {
                        appointmentService.DeleteAppointment(appointment.getAppointmentId());
                    }
                }
            }
        }
        doctor.setAvailableDays(newAvailability);
        doctorRepository.save(doctor);
    }


    public Map<String,String> getAvailability(int doctorId){
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        return doctor.getAvailableDays();
    }

    public List<Doctor> getDoctorForUrgentCases(){
        return  doctorRepository.findDoctorUrgentCases();
    }
}

