package com.company.pawpet.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "appointment")

public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int AppointmentId;
    float price;
    long duration;
    boolean booked = false;

    @JsonFormat(pattern = "yyyy-MM-dd")
   private LocalDate selectedDate;

    private String startTime;

    private String endTime;


    public DayOfWeek getDayOfWeek() {
        return selectedDate.getDayOfWeek();
    }

    @ManyToOne
    @JoinColumn(name = "AppUserId")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "ServiceId")
    private ServiceModel Service;

    @ManyToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "app_user_id")
    private Doctor doctor;

    public int getAppointmentId() {
        return AppointmentId;
    }

    public long getDuration() {
        if (startTime != null && endTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
            LocalTime startRange = LocalTime.parse(startTime, formatter);
            LocalTime endRange = LocalTime.parse(endTime, formatter);

            return Duration.between(startRange, endRange).toMinutes();
        }else{
            return 0;}
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public void setAppointmentId(int appointmentId) {
        AppointmentId = appointmentId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public ServiceModel getService() {
        return Service;
    }

    public void setService(ServiceModel service) {
        Service = service;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
