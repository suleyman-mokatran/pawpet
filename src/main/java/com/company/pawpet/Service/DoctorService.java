package com.company.pawpet.Service;


import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Doctor;
import com.company.pawpet.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

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

    public List<Doctor> findDoctorsBySpecialization(Doctor doctor){
        return doctorRepository.findDoctorBySpecialization(doctor.getSpecialization());
    }

    public Doctor findByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }
}

