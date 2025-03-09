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

    @ManyToMany(mappedBy = "orderItemList")
    private List<Product> productList ;

    @ManyToOne
    @JoinColumn(name = "OrderId")
    private Order order;

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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
