package com.company.pawpet.Service;

import com.company.pawpet.Model.Category;
import com.company.pawpet.Model.Company;
import com.company.pawpet.Model.Product;
import com.company.pawpet.Model.ProductProvider;
import com.company.pawpet.Repository.OrderItemRepository;
import com.company.pawpet.Repository.OrderRepository;
import com.company.pawpet.Repository.ProductRepository;
import com.company.pawpet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Autowired
    UserRepository appUserRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public void saveProduct(int categoryId,int ppId,Product product) {
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
        productRepository.save(newProduct);
        int id = newProduct.getProductId();
        updateProductStatus(id);

    }

    public List<Product> getAllProductsByPP(int id) {
        return productRepository.findProductsByProviderId(id);
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }


    public void updateProduct(int categoryId,int productId, Product productDetails) {
        Product existingProduct = productRepository.findById(productId).orElseThrow();

        Product productToUpdate = existingProduct;
        Category category = categoryService.findById(categoryId);

        productToUpdate.setProductName(productDetails.getProductName());
        productToUpdate.setDescription(productDetails.getDescription());
        productToUpdate.setPriceByColorAndSize(productDetails.getPriceByColorAndSize());
        productToUpdate.setImage(productDetails.getImage());
        productToUpdate.setProductCategory(category);
        productToUpdate.setStockByColorAndSize(productDetails.getStockByColorAndSize());
        productRepository.save(productToUpdate);
        updateProductStatus(productId);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String type){
        return productRepository.findProductByCategory(type);

    }

    public Product updateProductStatus(int productId){
        Product product = productRepository.findById(productId).orElseThrow();
        int stock=0;
        Map<String,Integer> stockMap = product.getStockByColorAndSize();
        for(String type : stockMap.keySet()){
            stock+=stockMap.get(type);
        }
        if(stock > 0){
            product.setStatus("available");
            productRepository.save(product);
        }
        else{
            product.setStatus("out of stock");
            productRepository.save(product);
        }
        return product;
    }

    public List<String> findProductsByCategory(){
        return productRepository.findMSCategoryKeysForProduct();
    }

    public int numberOfProductsByPP(int id){
        return productRepository.countProductsByProviderId(id);
    }

    public List<Map<String, Integer>> namesOfBuyers(int id) {
        List<Object[]> users = appUserRepository.findUserIdFirstAndLastNameByProviderId(id);
        List<Map<String, Integer>> buyers = new ArrayList<>();
        Set<String> seenNames = new HashSet<>();

        for (Object[] o : users) {
            if (o != null && o.length >= 3) {
                String fullName = o[1] + " " + o[2];
                int userId = (int) o[0];
                int count = appUserRepository.findCountOfOrderItemsByUserId(userId);

                if (!seenNames.contains(fullName)) {
                    buyers.add(Map.of(fullName, count));
                    seenNames.add(fullName);
                }
            }
        }
        return buyers;
    }

    public List<Map<String, Integer>> ratingsOfProducts(int id) {
        List<Product> products = productRepository.findProductsByProviderId(id);
        List<Map<String, Integer>> ratedProducts = new ArrayList<>();

        for (Product p : products) {
            int rate = reviewService.productRatingAverage(p.getProductId());
            String name = p.getProductName();
            ratedProducts.add(Map.of(name, rate));
        }

        return ratedProducts;
    }


    public String formatDateOnly(Timestamp timestamp) {
        LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        return localDate.format(formatter);
    }

    public List<Map<String, Integer>> getRevenueByDate(int providerId) {
        List<Object[]> rawResults = orderRepository.getDailyTotalPriceByProvider(providerId);
        List<Map<String, Integer>> result = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");

        for (Object[] row : rawResults) {
            LocalDate localDate;

            if (row[0] instanceof Timestamp ts) {
                localDate = ts.toLocalDateTime().toLocalDate();
            } else if (row[0] instanceof java.util.Date date) {
                localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                throw new IllegalArgumentException("Unsupported date type: " + row[0].getClass());
            }

            String formattedDate = localDate.format(formatter);
            Integer total = ((Number) row[1]).intValue();
            result.add(Map.of(formattedDate, total));
        }

        return result;
    }


    public List<Map<String, Integer>> productSales(int providerId) {
        List<Object[]> results = orderItemRepository.findProductSalesByProvider(providerId);
        List<Map<String, Integer>> sales = new ArrayList<>();

        for (Object[] row : results) {
            String productName = (String) row[0];
            Integer total = ((Number) row[1]).intValue();
            sales.add(Map.of(productName, total));
        }

        return sales;
    }

    public void setProductAsInActive(int id){
        Product product = productRepository.findById(id).orElseThrow();
        product.setStatus("inactive");
        productRepository.save(product);
    }








}