package com.company.pawpet.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "cartitems")

public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int CartItemId;
    int Quantity;
    float Price;

    @ManyToMany(mappedBy = "cartItemList")
    private List<Product> productList ;

    @ManyToOne
    @JoinColumn(name = "CartId")
    private Cart cart;
}
