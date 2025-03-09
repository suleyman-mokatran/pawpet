package com.company.pawpet.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "cart")

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int CartId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItemList;

    @OneToOne(mappedBy = "cart")
    private Order order;

    @OneToOne(mappedBy = "cart")
    private AppUser appUser;

}
