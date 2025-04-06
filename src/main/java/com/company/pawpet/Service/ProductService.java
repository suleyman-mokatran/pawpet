package com.company.pawpet.Service;

import com.company.pawpet.Model.Category;
import com.company.pawpet.Model.Company;
import com.company.pawpet.Model.Product;
import com.company.pawpet.Model.ProductProvider;
import com.company.pawpet.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CompanyService companyService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductProviderService productProviderService;

    public Product saveProduct(int categoryId,int ppId,int companyId,Product product) {
        Product newProduct = new Product();
        Category category = categoryService.findById(categoryId);
        Company company = companyService.getCompanyById(companyId).orElseThrow();
        ProductProvider pp = productProviderService.getPPById(ppId).orElseThrow();

        newProduct.setProductCategory(category);
        newProduct.setCompany(company);
        newProduct.setProductProvider(pp);
        newProduct.setImage(product.getImage());
        newProduct.setProductName(product.getProductName());
        newProduct.setDescription(product.getDescription());
        newProduct.setStock(product.getStock());
        newProduct.setPrice(product.getPrice());


        return productRepository.save(newProduct);
    }

    public List<Product> getAllProducts(int id) {
        return productRepository.findProductsByProviderId(id);
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }


    public Product updateProduct(int categoryId,int productId,int companyId, Product productDetails) {
        Product existingProduct = productRepository.findById(productId).orElseThrow();

        Product productToUpdate = existingProduct;
        Category category = categoryService.findById(categoryId);
        Company company = companyService.getCompanyById(companyId).orElseThrow();


        productToUpdate.setProductName(productDetails.getProductName());
        productToUpdate.setDescription(productDetails.getDescription());
        productToUpdate.setStock(productDetails.getStock());
        productToUpdate.setPrice(productDetails.getPrice());
        productToUpdate.setImage(productDetails.getImage());
        productToUpdate.setProductCategory(category);
        productToUpdate.setCompany(company);

        return productRepository.save(productToUpdate);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}