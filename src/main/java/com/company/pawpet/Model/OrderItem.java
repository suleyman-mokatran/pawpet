package com.company.pawpet.Model;

import jakarta.persistence.*;


import java.util.List;

@Entity
@Table(name = "orderitems")

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int OrderItemsId;
    int quantity;
    Float price;

    @ManyToOne
    @JoinColumn(name = "product_id") // Foreign key to the Product table
    private Product product;

    @ManyToOne
    @JoinColumn(name = "OrderId")
    private Order order;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getOrderItemsId() {
        return OrderItemsId;
    }

    public void setOrderItemsId(int orderItemsId) {
        OrderItemsId = orderItemsId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
