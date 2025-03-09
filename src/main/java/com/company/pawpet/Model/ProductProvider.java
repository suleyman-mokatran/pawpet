package com.company.pawpet.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity

public class ProductProvider extends AppUser {

    @OneToMany(mappedBy = "ProductProvider", cascade = CascadeType.ALL)
    private List<Product> productList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CompanyId", referencedColumnName = "CompanyId")
    private Company company;



}
