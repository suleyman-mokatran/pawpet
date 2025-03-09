package com.company.pawpet.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;  // Corrected field naming

    private String ProductName;  // Corrected field naming
    private String Description;
    private float Price;
    private int Stock;

    public Product() {
    }

    public Product(int productId, String productName, String description, float price, int stock) {
        this.productId = productId;
        ProductName = productName;
        Description = description;
        Price = price;
        Stock = stock;
    }

    @ManyToOne
    @JoinColumn(name = "ProductProviderId")
    private ProductProvider ProductProvider;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    @ManyToOne
    @JoinColumn(name = "Category")
    private Category ProductCategory;  // Corrected field naming

    @ManyToOne
    @JoinColumn(name = "Company")
    private Company Company;  // Corrected field naming

    @ManyToMany
    @JoinTable(
            name = "CartItem_Product",
            joinColumns = @JoinColumn(name = "ProductId"),
            inverseJoinColumns = @JoinColumn(name = "CartItemId")
    )
    private List<CartItem> cartItemList;

    @ManyToMany
    @JoinTable(
            name = "OrderItem_Product",
            joinColumns = @JoinColumn(name = "ProductId"),
            inverseJoinColumns = @JoinColumn(name = "OrderItemId")
    )
    private List<OrderItem> orderItemList;

    public ProductProvider getProductProvider() {
        return ProductProvider;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public Category getProductCategory() {
        return ProductCategory;
    }

    public Company getCompany() {
        return Company;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getDescription() {
        return Description;
    }

    public float getPrice() {
        return Price;
    }


    public int getStock() {
        return Stock;
    }

    public void setProductProvider(ProductProvider productProvider) {
        this.ProductProvider = productProvider;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public void setProductCategory(Category productCategory) {
        ProductCategory = productCategory;
    }

    public void setCompany(Company company) {
        this.Company = company;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setPrice(float price) {
        this.Price = price;
    }

    public void setStock(int stock) {
        this.Stock = stock;
    }


}
