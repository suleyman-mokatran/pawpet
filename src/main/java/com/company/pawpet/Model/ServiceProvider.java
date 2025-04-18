package com.company.pawpet.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.Map;

@Entity


public class ServiceProvider extends AppUser {

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ServiceModel> serviceList;

    String specialization;


    @ElementCollection
    private Map<String, String> availableDays;

    @OneToOne
    @JoinColumn(name = "CompanyId", referencedColumnName = "CompanyId")
    private Company company;

    public Map<String, String> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Map<String, String> availableDays) {
        this.availableDays = availableDays;
    }

    public List<ServiceModel> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ServiceModel> serviceList) {
        this.serviceList = serviceList;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}

