package com.company.pawpet.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity

@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int categoryId;


    private String name;

    String type;

    @OneToMany(mappedBy = "PetCategory", cascade = CascadeType.ALL)
    private List<Pet> petList;

    @OneToMany(mappedBy = "ProductCategory", cascade = CascadeType.ALL)
    private List<Product> productList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
