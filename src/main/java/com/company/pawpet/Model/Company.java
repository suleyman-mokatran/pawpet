package com.company.pawpet.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity

@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int CompanyId;
    String CompanyName;
    String CompanyAddress;

    @OneToMany(mappedBy = "Company", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> productList;

    @OneToOne(mappedBy = "company")
    private ServiceProvider serviceProvider;

    @OneToOne(mappedBy = "company")
    @JsonIgnore
    private ProductProvider productProvider;

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int companyId) {
        CompanyId = companyId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        CompanyAddress = companyAddress;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public ProductProvider getProductProvider() {
        return productProvider;
    }

    public void setProductProvider(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }
}
