package com.company.pawpet.Model;

import com.company.pawpet.Enum.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
public class Doctor extends AppUser {

    String specialization;

    @ElementCollection
    private Map<String, String> availableDays;
    int experienceYears;

    public Doctor() {
    }



    @OneToMany(mappedBy = "Doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointmentList;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Map<String, String> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Map<String, String> availableDays) {
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
