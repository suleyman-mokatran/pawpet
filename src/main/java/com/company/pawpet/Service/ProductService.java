package com.company.pawpet.Service;

import com.company.pawpet.Model.Category;
import com.company.pawpet.Model.Company;
import com.company.pawpet.Model.Product;
import com.company.pawpet.Model.ProductProvider;
import com.company.pawpet.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public Product saveProduct(int categoryId,int ppId,Product product) {
        Product newProduct = new Product();
        Category category = categoryService.findById(categoryId);
        ProductProvider pp = productProviderService.getPPById(ppId).orElseThrow();

        newProduct.setProductCategory(category);
        newProduct.setCompany(pp.getCompany());
        newProduct.setProductProvider(pp);
        newProduct.setImage(product.getImage());
        newProduct.setProductName(product.getProductName());
        newProduct.setDescription(product.getDescription());
        newProduct.setPriceByColorAndSize(product.getPriceByColorAndSize());
        newProduct.setStockByColorAndSize(product.getStockByColorAndSize());

        return productRepository.save(newProduct);
    }

    public List<Product> getAllProductsByPP(int id) {
        return productRepository.findProductsByProviderId(id);
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }


    public Product updateProduct(int categoryId,int productId, Product productDetails) {
        Product existingProduct = productRepository.findById(productId).orElseThrow();

        Product productToUpdate = existingProduct;
        Category category = categoryService.findById(categoryId);

        productToUpdate.setProductName(productDetails.getProductName());
        productToUpdate.setDescription(productDetails.getDescription());
        productToUpdate.setPriceByColorAndSize(productDetails.getPriceByColorAndSize());
        productToUpdate.setImage(productDetails.getImage());
        productToUpdate.setProductCategory(category);
        productToUpdate.setStockByColorAndSize(productDetails.getStockByColorAndSize());

        return productRepository.save(productToUpdate);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public int getOverALlStock(int productId){
        Product product = productRepository.findById(productId).orElseThrow();
        int stock=0;
        Map<String,Integer> stockMap = product.getStockByColorAndSize();
        for(String type : stockMap.keySet()){
            stock+=stockMap.get(type);
        }
        return stock;
    }
}