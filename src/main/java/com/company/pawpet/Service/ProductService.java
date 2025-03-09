package com.company.pawpet.Service;

import com.company.pawpet.Model.Product;
import com.company.pawpet.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }


    public Product updateProduct(int id, Product productDetails) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        Product productToUpdate = existingProduct.get();

        productToUpdate.setProductName(productDetails.getProductName());
        productToUpdate.setDescription(productDetails.getDescription());
        productToUpdate.setPrice(productDetails.getPrice());
        productToUpdate.setStock(productDetails.getStock());

        return productRepository.save(productToUpdate);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}