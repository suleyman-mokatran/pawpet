package com.company.pawpet.Service;


import com.company.pawpet.Model.Appointment;
import com.company.pawpet.Repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    public Appointment addNewAppointment(Appointment appointment){

        return appointmentRepository.save(appointment);
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
        appointmentToUpdate.setStartTime(appointment.getStartTime());
        appointmentToUpdate.setEndTime(appointment.getEndTime());
        appointmentToUpdate.setDuration(appointment.getDuration());
        appointmentToUpdate.setStatus(appointment.getStatus());
        appointmentToUpdate.setService(appointment.getService());
        appointmentToUpdate.setDoctor(appointment.getDoctor());
        appointmentToUpdate.setAppUser(appointment.getAppUser());

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
}

