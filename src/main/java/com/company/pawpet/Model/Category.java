package com.company.pawpet.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity

@Table(name = "categories")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "categoryId")

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int categoryId;

    @ElementCollection
    @JsonProperty("MSCategory")
    private Map<String , String> MSCategory;

    String type;

    public Category() {
    }

    @OneToMany(mappedBy = "PetCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Pet> petList;

    @OneToMany(mappedBy = "ProductCategory", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> productList;

    @OneToMany(mappedBy = "serviceCategory", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ServiceModel> serviceList;

    public List<ServiceModel> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ServiceModel> serviceList) {
        this.serviceList = serviceList;
    }

    public Map<String, String> getMSCategory() {
        return MSCategory;
    }

    public void setMSCategory(Map<String, String> MSCategory) {
        this.MSCategory = MSCategory;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
