package com.company.pawpet.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity

@Table(name = "services")

public class ServiceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ServiceId;

    String Name;
    String Description;
    float Price;

    @ManyToOne
    @JoinColumn(name = "Category")
    private Category serviceCategory;

    @OneToMany(mappedBy = "Service", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointmentList;

    @OneToMany(mappedBy = "Service", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviewList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CompanyId", referencedColumnName = "CompanyId")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "ServiceProviderId")
    private ServiceProvider serviceProvider;

    public Category getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(Category serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public int getServiceId() {
        return ServiceId;
    }

    public void setServiceId(int serviceId) {
        ServiceId = serviceId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
