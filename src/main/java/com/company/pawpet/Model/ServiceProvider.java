package com.company.pawpet.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity


public class ServiceProvider extends AppUser {

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private List<ServiceModel> serviceList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CompanyId", referencedColumnName = "CompanyId")
    private Company company;

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


}

