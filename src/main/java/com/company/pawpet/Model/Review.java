package com.company.pawpet.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "reviews")

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ReviewId;
    double Rating;

    @Lob
    String Comment;
    LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "appUser")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "Service")
    private ServiceModel Service;

    @ManyToOne
    @JoinColumn(name = "Doctor")
    private Doctor Doctor;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public com.company.pawpet.Model.Doctor getDoctor() {
        return Doctor;
    }

    public void setDoctor(com.company.pawpet.Model.Doctor doctor) {
        Doctor = doctor;
    }

    public int getReviewId() {
        return ReviewId;
    }

    public void setReviewId(int reviewId) {
        ReviewId = reviewId;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
