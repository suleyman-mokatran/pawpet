package com.company.pawpet.Controller;

import com.company.pawpet.Model.*;
import com.company.pawpet.Model.PasswordUpdateRequest;
import com.company.pawpet.Repository.ProductProviderRepository;
import com.company.pawpet.Service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pp")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('PP')")
public class PPController {

    @Autowired
    ProductProviderService productProviderService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CompanyService companyService;

    @Autowired
    AppUserService appUserService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ProductProviderRepository productProviderRepository;

    @Autowired
    ReviewService reviewService;

    @GetMapping("/profile")
    public ResponseEntity<ProductProvider> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProductProvider profile = productProviderService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) {

        String oldPassword = passwordUpdateRequest.getOldPassword();
        String newPassword = passwordUpdateRequest.getNewPassword();
        String username = userDetails.getUsername();

        ProductProvider pp = productProviderRepository.findByUsername(username);

        if (pp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PP not found.");
        }

        if (!passwordEncoder.matches(oldPassword, pp.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
        }

        pp.setPassword(passwordEncoder.encode(newPassword));
        productProviderRepository.save(pp);

        return ResponseEntity.ok("Password updated successfully.");
    }

    @PutMapping("/updatepp/{id}")
    public ResponseEntity<?> updatePp(@PathVariable int id, @Valid @RequestBody ProductProvider pp, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        ProductProvider savedPp = productProviderService.updateProductProvider(id, pp);
        return ResponseEntity.ok(savedPp);
    }

    @GetMapping("/getpp/{id}")
    public ResponseEntity<ProductProvider> getPpById(@PathVariable int id) {

        ProductProvider pp = productProviderService.getPPById(id).orElseThrow();
        return ResponseEntity.ok(pp);
    }

    @GetMapping("/getallcompanies")
    public List<Company> getAllCompanies() {
        return companyService.findAllCompanies();
    }

    @GetMapping("/getproducts/{id}")
    public ResponseEntity<List<Product>> getAllProducts(@PathVariable int id) {
        return ResponseEntity.ok(productService.getAllProductsByPP(id));
    }

    @PostMapping("/addproduct/{categoryId}/{ppId}")
    public void addNewProduct(@PathVariable int categoryId, @PathVariable int ppId, @RequestBody Product product) {
        productService.saveProduct(categoryId, ppId, product);
    }

    @GetMapping("/getproductcategories")
    public List<Map<String, String>> findProductCategories() {
        return categoryService.findProductCategory();
    }

    @DeleteMapping("/deleteproduct/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    @PutMapping("/inactiveproduct/{id}")
    public void inactiveProduct(@PathVariable int id){
   productService.setProductAsInActive(id);
    }

    @PutMapping("/activeproduct/{id}")
    public void activeProduct(@PathVariable int id){
        productService.updateProductStatus(id);
    }


    @PutMapping("/updateproduct/{categoryId}/{productId}")
    public void updateProduct(@PathVariable int categoryId, @PathVariable int productId, @RequestBody Product product) {
        productService.updateProduct(categoryId, productId, product);
    }

    @GetMapping("/getproduct/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        return ResponseEntity.ok(productService.getProductById(id).orElseThrow());
    }

    @GetMapping("/getorders/{ppId}")
    public ResponseEntity<Map<Integer,List<OrderItem>>> getOrders(@PathVariable int ppId) {
        List<OrderItem> orderItemList = orderItemService.getOrder(ppId);

        Map<Integer, List<OrderItem>> groupedByOrder = orderItemList.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getOrder().getOrderId()));

        return ResponseEntity.ok(groupedByOrder);
    }

    @GetMapping("/getorderinfo/{orderid}")
    public ResponseEntity<Order> getOrderInfo(@PathVariable int orderid){
      Order order = orderService.getOrderById(orderid).orElseThrow();
    return ResponseEntity.ok(order);
    }

    @GetMapping("/getorderitems/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable int orderId){
        return ResponseEntity.ok(orderItemService.getOrderItems(orderId));
    }

    @PutMapping("/markasdone/{itemId}")
    public ResponseEntity<String> markItemAsDone(@PathVariable int itemId){
        orderItemService.markItemAsDone(itemId);
        return ResponseEntity.ok("Done");
    }

    @PutMapping("/markasundone/{itemId}")
    public ResponseEntity<String> markItemAsUnDone(@PathVariable int itemId){
        orderItemService.markItemAsUnDone(itemId);
        return ResponseEntity.ok("UnDone");
    }

    @GetMapping("/getorder/{orderid}")
    public ResponseEntity<Order> getOrder(@PathVariable int orderid){
        return ResponseEntity.ok(orderService.getOrderById(orderid).orElseThrow());
    }

    @GetMapping("/getnotifications/{id}")
    public List<Notification> getNotifications(@PathVariable int id) {
        return notificationService.getAllNotificationsForUser(id);
    }

    @GetMapping("/unreadCount/{id}")
    public int getUnreadCount(@PathVariable int id) {
        return notificationService.getUnreadCount(id);
    }

    @PutMapping("/markRead/{id}")
    public void markAsRead(@PathVariable int id) {
        notificationService.markAsRead(id);
    }

    @DeleteMapping("/deletenotification/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable int id){
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/getnumberofproductsbypp/{id}")
    public ResponseEntity<Integer> getNumberOfProducts(@PathVariable int id){
    return ResponseEntity.ok(productService.numberOfProductsByPP(id));
    }

    @GetMapping("/productaverage/{id}")
    public ResponseEntity<Integer> productOverAllRatings(@PathVariable int id){
        return ResponseEntity.ok(reviewService.productRatingAverage(id));
    }

    @GetMapping("/usersorder/{id}")
    public ResponseEntity<List<Map<String,Integer>>> namesAndOrders(@PathVariable int id){
        return ResponseEntity.ok(productService.namesOfBuyers(id));
    }

    @GetMapping("/ratings/{id}")
    public ResponseEntity<List<Map<String,Integer>>> ratingsOfProductByName(@PathVariable int id){
        return ResponseEntity.ok(productService.ratingsOfProducts(id));
    }

    @GetMapping("/getdailyincome/{id}")
    public ResponseEntity<List<Map<String,Integer>>> getDailyIncome(@PathVariable int id){
        return ResponseEntity.ok(productService.getRevenueByDate(id));
    }

    @GetMapping("/getproductssales/{id}")
    public ResponseEntity<List<Map<String,Integer>>> getProductsSales(@PathVariable int id){
        return ResponseEntity.ok(productService.productSales(id));
    }




}

