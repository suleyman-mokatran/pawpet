package com.company.pawpet.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Doctor extends AppUser {

    @Column(nullable = true)
    String specialization;

    @ElementCollection
    private List<String> availableDays;
    int experienceYears;


    @OneToMany(mappedBy = "Doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointmentList;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<String> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(List<String> availableDays) {
        this.availableDays = availableDays;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }


    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }
}
