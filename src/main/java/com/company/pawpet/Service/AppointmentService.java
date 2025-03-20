package com.company.pawpet.Service;


import com.company.pawpet.Model.Appointment;
import com.company.pawpet.Model.Doctor;
import com.company.pawpet.Repository.AppointmentRepository;
import com.company.pawpet.Repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DoctorRepository doctorRepository;

    public Appointment addNewAppointment(int id ,Appointment appointment){

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

        return appointmentRepository.save(savedAppointment);
    }

    public Optional<Appointment> getAppointment(int appointmentId){
        return appointmentRepository.findById(appointmentId);
    }

    public Appointment updateAppointment(int id , Appointment appointment){
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);

        if (existingAppointment.isEmpty()) {
            throw new RuntimeException("Appointment with ID " + id + " not found.");
        }

        Appointment appointmentToUpdate = existingAppointment.get();

        appointmentToUpdate.setPrice(appointment.getPrice());
        appointmentToUpdate.setBooked(appointment.isBooked());
        appointmentToUpdate.setStartTime(appointment.getStartTime());
        appointmentToUpdate.setEndTime(appointment.getEndTime());

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

    public String getWorkingTimeByDay(String day,int id){
        return doctorRepository.findByDayAndDoctorId(day,id);
    }


}

