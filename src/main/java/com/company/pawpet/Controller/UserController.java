package com.company.pawpet.Controller;

import com.company.pawpet.Model.*;
import com.company.pawpet.PaymentRequest;
import com.company.pawpet.Repository.CartRepository;
import com.company.pawpet.notification.NotificationHandler;
import com.company.pawpet.PasswordUpdateRequest;
import com.company.pawpet.Repository.UserRepository;
import com.company.pawpet.Service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final NotificationHandler notificationHandler;

    public UserController(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @Autowired
    AppUserService appUserService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    ProductService productService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

   @Autowired
    PetService petService;

   @Autowired
    AppointmentService appointmentService;

   @Autowired
   CartService cartService;

   @Autowired
   CartItemService cartItemService;

   @Autowired
   OrderService orderService;

   @Autowired
   OrderItemService orderItemService;

   @Autowired
    ServiceService serviceService;

    @GetMapping("/profile")
    public ResponseEntity<AppUser> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        AppUser profile = appUserService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) {

        String oldPassword = passwordUpdateRequest.getOldPassword();
        String newPassword = passwordUpdateRequest.getNewPassword();
        String username = userDetails.getUsername();

        AppUser user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully.");
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @Valid @RequestBody AppUser appUser, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        AppUser savedUser = appUserService.updateUser(id,appUser);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/getuser/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable int id) {
        return appUserService.getUserById(id).map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @GetMapping("/getpets/{id}")
    public ResponseEntity<List<Pet>> getAllPets(@PathVariable int id) {
        List<Pet> pets = petService.getAllPets(id);
        return ResponseEntity.ok(pets);}


    @DeleteMapping("/deletepet/{id}")
    public void deletePet(@PathVariable int id ){
        petService.deletePet(id);
    }

    @PostMapping("/addpet/{id}/{categoryId}")
    public ResponseEntity<?> addNewPet(@PathVariable int id ,@PathVariable int categoryId, @RequestBody Pet pet, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        Pet savedPet = petService.addNewPet(pet,id,categoryId);
        return ResponseEntity.ok(savedPet);
    }

    @PutMapping("/updatepet/{id}/{categoryId}")
    public ResponseEntity<?> updatePet(@PathVariable int id,@PathVariable int categoryId, @RequestBody Pet pet, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Pet savedPet = petService.updatePet(id,categoryId,pet);
        return ResponseEntity.ok(savedPet);
    }
    @GetMapping("/getpet/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable int id) {
        return petService.getPetById(id).map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
    }

    @GetMapping("/getpetcategories")
    public List<Map<String,String>> findPetCategories(){
        return categoryService.findPetCategory();
    }

    @GetMapping("/getcategory/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
        Category category=  categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/getspecializations")
    public ResponseEntity<List<String>> getSpecializations() {
        List<String> specializations =doctorService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }

    @GetMapping("/getspecializeddoctors/{specialization}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<Doctor> doctors =doctorService.findDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/getappointments/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable int id){
        List<Appointment> appointmentList = appointmentService.findAppointmentsByDoctor(id);
        List<Appointment> filteredAppointments = new ArrayList<>();

        for(var appointment : appointmentList){
            if(!appointment.isBooked()){
                filteredAppointments.add(appointment);
            }
        }
        return ResponseEntity.ok(filteredAppointments);
    }

    @GetMapping("/getappointment/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable int id){
        Appointment appointment = appointmentService.getAppointment(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/getpetbycategory/{id}/{category}")
    public ResponseEntity<List<Pet>> getPetByCategory(@PathVariable int id, @PathVariable String category) {
        List<Pet> pets = petService.getAllPets(id);
        List<Pet> same = new ArrayList<>();
        for (var pet : pets) {
            Map<String, String> entry = pet.getPetCategory().getMSCategory();
            if (!entry.isEmpty()) {
                String firstKey = entry.keySet().iterator().next();
                if (firstKey.equals(category)) {
                    same.add(pet);
                }
            }
        }
            return ResponseEntity.ok(same);
    }

    @GetMapping("/getdoctor/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable int id){
        Doctor doctor = doctorService.findById(id).orElseThrow();
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/confirmbooking/{userId}/{doctorId}/{petId}/{appointmentId}")
    public ResponseEntity<String> confirmBooking(@PathVariable int userId,@PathVariable int doctorId,@PathVariable int petId,@PathVariable int appointmentId) throws IOException {
           Appointment appointment =  appointmentService.bookAppointment(userId,doctorId,petId,appointmentId);
        try {
            notificationHandler.sendNotificationToDoctor(doctorId, "New appointment booked!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Appointment booked successfully!");
    }

    @GetMapping("/getuserappointments/{id}")
    public ResponseEntity<List<Appointment>> getUserAppointments(@PathVariable int id){
        List<Appointment> appointmentList = appointmentService.findAppointmentsByUserId(id);
        return ResponseEntity.ok(appointmentList);
    }

    @PutMapping("/unbookappointment/{id}")
    public ResponseEntity<Appointment> unbookAppointment(@PathVariable int id) {
        return ResponseEntity.ok(appointmentService.unbookAppointment(id));
    }

    @PutMapping("/rescheduleappointment/{oldId}/{newId}/{doctorId}")
    public ResponseEntity<String> rescheduleAppointment(@PathVariable int oldId, @PathVariable int newId, @PathVariable int doctorId) {
        try {
            appointmentService.rescheduleBookedAppointment(oldId, newId);

            notificationHandler.sendNotificationToDoctor(doctorId, "An appointment has rescheduled!");

            return ResponseEntity.ok("Appointment rescheduled successfully!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending notification: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error rescheduling appointment: " + e.getMessage());
        }
    }

    @GetMapping("/getproducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/getproduct/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        return ResponseEntity.ok(productService.getProductById(id).orElseThrow());
    }

    @PostMapping("/addtocart/{userId}/{productId}")
    public ResponseEntity<Cart> addToCart(@PathVariable int userId,@PathVariable int productId, @RequestBody CartItem cartItem) {
        int cartId;
        AppUser appUser = appUserService.getUserById(userId).orElseThrow();
        if(appUser.getCart() == null){
        Cart userCart = cartService.saveCart(userId);
            cartId = userCart.getCartId();
            CartItem savedItem = cartItemService.saveCartItem(cartId,productId, cartItem);
            userCart.getCartItemList().add(savedItem);
            Cart updatedCart = cartService.updateCart(cartId, userCart);
            return ResponseEntity.ok(updatedCart);
        }
        else{
            int existingCardId = appUser.getCart().getCartId();
            Cart existCart = cartService.getCartById(existingCardId).orElseThrow();
            for(CartItem ci : existCart.getCartItemList()){
                if(ci.getProduct().getProductId() == productId
                        && ci.getColor().equals(cartItem.getColor())
                        && ci.getSize().equals(cartItem.getSize())){
                    int sum = ci.getQuantity()+cartItem.getQuantity();
                    int cartItemId = ci.getCartItemId();
                   CartItem existCartItem =  cartItemService.updateCartItem(cartItemId,sum);
                    existCart.getCartItemList().add(existCartItem);
                    Cart updatedCart = cartService.updateCart(existingCardId, existCart);
                    return ResponseEntity.ok(updatedCart);
                }
            }
            CartItem savedItem = cartItemService.saveCartItem(existingCardId,productId, cartItem);
            existCart.getCartItemList().add(savedItem);
            Cart updatedCart = cartService.updateCart(existingCardId, existCart);
            return ResponseEntity.ok(updatedCart);
        }
    }


    @GetMapping("/getnumberofcartitems/{userId}")
    public ResponseEntity<Integer> getNumberOfCartItems(@PathVariable int userId) {
        AppUser appUser = userRepository.findById(userId).orElseThrow();

        Cart cart = appUser.getCart();
        if (cart == null) {
            return ResponseEntity.ok(0);
        }

        List<CartItem> items = cart.getCartItemList();

        return ResponseEntity.ok(items.size());
    }

    @GetMapping("/getcart/{id}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable int id){
        AppUser appUser = userRepository.findById(id).orElseThrow();
        Cart cart = appUser.getCart();
        List<CartItem> items = cart.getCartItemList();
        return ResponseEntity.ok(items);
    }

    @PutMapping("/incrementitem/{cartItemId}")
    public void incrementItem(@PathVariable int cartItemId) {
        CartItem cartItem = cartItemService.getCartItemById(cartItemId).orElseThrow();

        int plus = cartItem.getQuantity() + 1;
        cartItemService.updateCartItem(cartItemId, plus);
       Cart cart = cartItem.getCart();
       Cart newCart = cartService.updateCart(cart.getCartId(),cart);
    }

    @PutMapping("/decrementitem/{cartItemId}")
    public void decrementItem(@PathVariable int cartItemId) {
        CartItem cartItem = cartItemService.getCartItemById(cartItemId).orElseThrow();
        CartItem extend = cartItem;
        int quantity = cartItem.getQuantity();
        if (quantity == 1) {
            cartItemService.deleteCartItem(cartItemId);
            Cart cart = extend.getCart();
            Cart newCart = cartService.updateCart(cart.getCartId(),cart);
        } else {
            int minus = cartItem.getQuantity() - 1;
            cartItemService.updateCartItem(cartItemId, minus);
            Cart cart = cartItem.getCart();
            Cart newCart = cartService.updateCart(cart.getCartId(),cart);
        }
    }

    @PostMapping("/pay/{userid}")
    public ResponseEntity<Map<String, String>> makeFakePayment(@PathVariable int userid,@RequestBody PaymentRequest request) throws IOException {
        if ("4242424242424242".equals(request.getCardNumber())) {
            int orderId;
            List<Integer> ppIds = new ArrayList<>();
            AppUser appUser = appUserService.getUserById(userid).orElseThrow();
            Cart cart = appUser.getCart();
            List<Integer> cartItems = request.getCartItems();
                Order userOrder = orderService.saveOrder(userid);
                userOrder.setPhone(request.getPhone());
                userOrder.setLocation(request.getLocation());
                userOrder.setFullName(request.getFullName());
                userOrder.setEmail(request.getEmail());
                userOrder.setPay("Paid");
                orderId = userOrder.getOrderId();
                for(int item : cartItems){
                    CartItem cartItem = cartItemService.getCartItemById(item).orElseThrow();
                    OrderItem orderItem = new OrderItem();
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setQuantity(cartItem.getQuantity());
                    OrderItem savedItem = orderItemService.saveOrderItem(orderId,cartItem.getProduct().getProductId(), orderItem);
                    Product product = savedItem.getProduct();
                    int newPP = product.getProductProvider().getAppUserId();
                   if(ppIds.isEmpty()){
                       ppIds.add(newPP);
                       notificationHandler.sendNotificationToPP(newPP, "New Order!");
                   }
                   else{
                       if(!ppIds.contains(newPP)){
                           ppIds.add(newPP);
                           notificationHandler.sendNotificationToPP(newPP, "New Order!");
                       }
                   }

                    String colorAndSize = cartItem.getColor() + '-' + cartItem.getSize();
                    int productQuantity = product.getStockByColorAndSize().get(colorAndSize);
                    Map<String, Integer> stockMap = product.getStockByColorAndSize();
                    stockMap.put(colorAndSize,productQuantity - cartItem.getQuantity());
                    product.setStockByColorAndSize(stockMap);
                    productService.updateProduct(product.getProductCategory().getCategoryId(),product.getProductId(),product);
                    userOrder.getOrderItemList().add(savedItem);
                    cartItem.setCart(null);
                    cart.getCartItemList().remove(cartItem);
                    cartItemService.deleteCartItem(item);
                }
                Order updatedOrder = orderService.updateOrder(orderId,userOrder);
            if(cart.getCartItemList() == null || cart.getCartItemList().isEmpty()) {
                appUser.setCart(null);
                userRepository.save(appUser);
            }

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Payment accepted"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", "Invalid card number"
            ));
        }
    }

        @GetMapping("/getstock/{productId}")
        public ResponseEntity<Integer> getProductStock(@PathVariable int productId){
        return ResponseEntity.ok(productService.getOverALlStock(productId));
        }

        @GetMapping("/order/{userid}")
        public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable int userid){
        return ResponseEntity.ok(orderService.getUserOrders(userid));
        }

    @GetMapping("/getorderitems/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable int orderId){
        return ResponseEntity.ok(orderItemService.getOrderItems(orderId));
    }

    @GetMapping("/getallservices")
    public ResponseEntity<List<ServiceModel>> getAllServices(){
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("/getserviceappointments/{id}")
    public ResponseEntity<List<Appointment>> getServiceAppointments(@PathVariable int id){
        List<Appointment> appointmentList = appointmentService.findAppointmentsByService(id);
        List<Appointment> filteredAppointments = new ArrayList<>();

        for(var appointment : appointmentList){
            if(!appointment.isBooked()){
                filteredAppointments.add(appointment);
            }
        }
        return ResponseEntity.ok(filteredAppointments);
    }

    @PutMapping("/confirmservicebooking/{userId}/{serviceId}/{petId}/{appointmentId}")
    public ResponseEntity<String> confirmServiceBooking(@PathVariable int userId,@PathVariable int serviceId,@PathVariable int petId,@PathVariable int appointmentId) throws IOException {
        Appointment appointment =  appointmentService.bookServiceAppointment(userId,serviceId,petId,appointmentId);
        ServiceModel serviceModel = serviceService.getServiceById(serviceId);
        int spId = serviceModel.getServiceProvider().getAppUserId();
        try {
            notificationHandler.sendNotificationToSP(spId, "New appointment booked!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Appointment booked successfully!");
    }

    @GetMapping("/getservice/{id}")
    public ResponseEntity<ServiceModel> getServiceById(@PathVariable int id){
        ServiceModel service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @PutMapping("/rescheduleserviceappointment/{oldId}/{newId}/{serviceId}")
    public ResponseEntity<String> rescheduleServiceAppointment(@PathVariable int oldId, @PathVariable int newId, @PathVariable int serviceId) {
        try {
            appointmentService.rescheduleBookedAppointment(oldId, newId);
            ServiceModel serviceModel = serviceService.getServiceById(serviceId);
            int spId = serviceModel.getServiceProvider().getAppUserId();
            notificationHandler.sendNotificationToSP(spId, "An appointment has rescheduled!");

            return ResponseEntity.ok("Appointment rescheduled successfully!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending notification: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error rescheduling appointment: " + e.getMessage());
        }
    }

    @PostMapping("/cod/{userid}")
    public ResponseEntity<Map<String, String>> makeOrderCod(@PathVariable int userid,@RequestBody PaymentRequest request) throws IOException {
            int orderId;
            List<Integer> ppIds = new ArrayList<>();
            AppUser appUser = appUserService.getUserById(userid).orElseThrow();
            Cart cart = appUser.getCart();
            List<Integer> cartItems = request.getCartItems();
            Order userOrder = orderService.saveOrder(userid);
            userOrder.setPhone(request.getPhone());
            userOrder.setLocation(request.getLocation());
            userOrder.setFullName(request.getFullName());
            userOrder.setEmail(request.getEmail());
            userOrder.setPay("Cash On Delivery");
            orderId = userOrder.getOrderId();
            for(int item : cartItems){
                CartItem cartItem = cartItemService.getCartItemById(item).orElseThrow();
                OrderItem orderItem = new OrderItem();
                orderItem.setPrice(cartItem.getPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                OrderItem savedItem = orderItemService.saveOrderItem(orderId,cartItem.getProduct().getProductId(), orderItem);
                Product product = savedItem.getProduct();
                int newPP = product.getProductProvider().getAppUserId();
                if(ppIds.isEmpty()){
                    ppIds.add(newPP);
                    notificationHandler.sendNotificationToPP(newPP, "New Order!");
                }
                else{
                    if(!ppIds.contains(newPP)){
                        ppIds.add(newPP);
                        notificationHandler.sendNotificationToPP(newPP, "New Order!");
                    }
                }

                String colorAndSize = cartItem.getColor() + '-' + cartItem.getSize();
                int productQuantity = product.getStockByColorAndSize().get(colorAndSize);
                Map<String, Integer> stockMap = product.getStockByColorAndSize();
                stockMap.put(colorAndSize,productQuantity - cartItem.getQuantity());
                product.setStockByColorAndSize(stockMap);
                productService.updateProduct(product.getProductCategory().getCategoryId(),product.getProductId(),product);
                userOrder.getOrderItemList().add(savedItem);
                cartItem.setCart(null);
                cart.getCartItemList().remove(cartItem);
                cartItemService.deleteCartItem(item);
            }
            Order updatedOrder = orderService.updateOrder(orderId,userOrder);
            if(cart.getCartItemList() == null || cart.getCartItemList().isEmpty()) {
                appUser.setCart(null);
                userRepository.save(appUser);
            }

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Cash on delivery accepted"
            ));

    }




}