package com.company.pawpet.Model;

import com.company.pawpet.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviewList;

    public Doctor() {
    }

    @Override
    public List<Review> getReviewList() {
        return reviewList;
    }

    @Override
    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
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
