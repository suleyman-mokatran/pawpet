package com.company.pawpet.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;  // Corrected field naming

    private String ProductName;  // Corrected field naming
    private String Description;

    @Lob
    private byte[] image;

   @ElementCollection
    Map<String,Integer> stockByColorAndSize;

   @ElementCollection
   Map<String, Double> priceByColorAndSize;

    public Product() {
    }

    public Product(int productId, String productName, String description, Map<String,Double> priceByColorAndSize,Map<String,Integer> stockByColorAndSize) {
        this.productId = productId;
        ProductName = productName;
        Description = description;
        this.priceByColorAndSize = priceByColorAndSize;
        this.stockByColorAndSize = stockByColorAndSize;
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
    private Company Company;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CartItem> cartItemList;


    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItemList;



    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

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

    public Map<String, Double> getPriceByColorAndSize() {
        return priceByColorAndSize;
    }

    public void setPriceByColorAndSize(Map<String, Double> priceByColorAndSize) {
        this.priceByColorAndSize = priceByColorAndSize;
    }

    public Map<String, Integer> getStockByColorAndSize() {
        return stockByColorAndSize;
    }

    public void setStockByColorAndSize(Map<String, Integer> stockByColorAndSize) {
        this.stockByColorAndSize = stockByColorAndSize;
    }
}
