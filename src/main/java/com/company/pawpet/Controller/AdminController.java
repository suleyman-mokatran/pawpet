package com.company.pawpet.Controller;

import com.company.pawpet.Model.*;
import com.company.pawpet.Model.PasswordUpdateRequest;
import com.company.pawpet.Repository.AdminRepository;
import com.company.pawpet.Service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ProductService productService;

    @Autowired
    ServiceService serviceService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    PetService petService;

    @Autowired
    PostService postService;

    @Autowired
    OrderService orderService;



    @GetMapping("/profile")
    public ResponseEntity<AppUser> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        AppUser profile = adminService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest, @AuthenticationPrincipal UserDetails userDetails) {

        String oldPassword = passwordUpdateRequest.getOldPassword();
        String newPassword = passwordUpdateRequest.getNewPassword();
        String username = userDetails.getUsername();

        AppUser user = adminService.getUserByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(user);
        return ResponseEntity.ok("Password updated successfully.");
    }

    @PostMapping("/adddoctor")
    public ResponseEntity<?> addNewDoctor(@Valid @RequestBody Doctor doctor, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Doctor savedDoctor = doctorService.addNewDoctor(doctor);
        return ResponseEntity.ok(savedDoctor);
    }

    @PutMapping("/updatedoctor/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable int id,@Valid @RequestBody Doctor doctor, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Doctor savedDoctor = doctorService.updateDoctor(id,doctor);
        return ResponseEntity.ok(savedDoctor);
    }

    @GetMapping("/getdoctor/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable int id) {
        return doctorService.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
    }

    @DeleteMapping("/deletedoctor/{id}")
    public void deleteDoctor(@PathVariable int id ){
        doctorService.deleteDoctor(id);
    }

    @GetMapping("/getdoctors")
    public List<Doctor> getAllDoctors(){
        return doctorService.getAllDoctors();
    }

    @Autowired
    AdminService adminService;

    @PostMapping("/addadmin")
    public ResponseEntity<?> addNewAdmin(@Valid @RequestBody AppUser admin, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        AppUser savedAdmin = adminService.addNewAdmin(admin);
        return ResponseEntity.ok(savedAdmin);
    }
    @PutMapping("/updateadmin/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable int id,@Valid @RequestBody AppUser admin, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        AppUser savedAdmin = adminService.updateAdmin(id,admin);
        return ResponseEntity.ok(savedAdmin);
    }

    @GetMapping("/getadmin/{id}")
    public ResponseEntity<AppUser> getAdminById(@PathVariable int id) {
        return adminService.getAdminById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
    }

    @DeleteMapping("/deleteadmin/{id}")
    public void deleteAdmin(@PathVariable int id ){
        adminService.deleteAdmin(id);
    }

    @GetMapping("/getadmins")
    public List<AppUser> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @Autowired
    CategoryService categoryService;

    @PostMapping("/addpetcategory")
    public ResponseEntity<?> addNewPetCategory( @RequestBody @Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Category savedCategory = categoryService.addNewPetCategory(category);
        return ResponseEntity.ok(savedCategory);
    }

    @PostMapping("/addservicecategory")
    public ResponseEntity<?> addNewServiceCategory( @RequestBody @Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Category savedCategory = categoryService.addNewServiceCategory(category);
        return ResponseEntity.ok(savedCategory);
    }

    @PostMapping("/addproductcategory")
    public ResponseEntity<?> addNewProductCategory( @RequestBody @Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Category savedCategory = categoryService.addNewProductCategory(category);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping("/getcategory/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
        Category petCategory =  categoryService.findById(id);
        return ResponseEntity.ok(petCategory);
    }

    @GetMapping("/getpetcategories")
    public List<Map<String,String>> findPetCategories(){
        return categoryService.findPetCategory();
    }

    @GetMapping("/getservicecategories")
    public List<Map<String,String>> findServiceCategories(){
        return categoryService.findServiceCategory();
    }

    @GetMapping("/getproductcategories")
    public List<Map<String,String>> findProductCategories(){
        return categoryService.findProductCategory();
    }

    @PutMapping("/updatecategory/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable int id,@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.updateCategory(id,category));
    }

    @DeleteMapping("/deletecategory/{id}")
    public void deleteCategory(@PathVariable int id){
        categoryService.deleteCategory(id);
    }

    @Autowired
    AppUserService appUserService;

    @PostMapping("/adduser")
    public ResponseEntity<?> addNewUser(@Valid @RequestBody AppUser user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        AppUser savedUser = appUserService.addNewUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id,@Valid @RequestBody AppUser user, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        AppUser savedUser = appUserService.updateUser(id,user);
        return ResponseEntity.ok(savedUser);
    }
    @GetMapping("/getuser/{id}")

    public ResponseEntity<AppUser> getUserById(@PathVariable int id) {
        return appUserService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @DeleteMapping("/deleteuser/{id}")
    public void deleteUser(@PathVariable int id ){
        appUserService.deleteUser(id);
    }

    @GetMapping("/getallusers")
    public List<AppUser> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @Autowired
    ServiceProviderService serviceProviderService;

    @PostMapping("/addsp/{id}")
    public ResponseEntity<?> addNewSP(@PathVariable int id,@Valid @RequestBody ServiceProvider sp, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        ServiceProvider savedsp = serviceProviderService.addNewSP(id,sp);
        return ResponseEntity.ok(savedsp);
    }

    @DeleteMapping("/deletesp/{id}")
    public void deleteSP(@PathVariable int id ){
        serviceProviderService.deleteServiceProvider(id);
    }

    @GetMapping("/getallsp")
    public List<ServiceProvider> getAllSP() {
        return serviceProviderService.getAllServiceProviders();
    }

    @GetMapping("/getsp/{id}")
    public ResponseEntity<ServiceProvider> getSpById(@PathVariable int id) {
        return serviceProviderService.getServiceProviderById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sp not found"));
    }

    @Autowired
    ProductProviderService productProviderService;

    @PostMapping("/addpp/{id}")
    public ResponseEntity<?> addNewPP(@Valid @RequestBody ProductProvider pp,@PathVariable int id, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        ProductProvider savedpp = productProviderService.addNewPP(pp,id);
        return ResponseEntity.ok(savedpp);
    }

    @GetMapping("/getpp/{id}")
    public ResponseEntity<ProductProvider> getPPById(@PathVariable int id) {
        return productProviderService.getPPById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PP not found"));
    }

    @DeleteMapping("/deletepp/{id}")
    public void deletePP(@PathVariable int id ){
        productProviderService.deleteProductProvider(id);
    }

    @GetMapping("/getallpp")
    public List<ProductProvider> getAllPP() {
        return productProviderService.getAllProductProviders();
    }

    @Autowired
    CompanyService companyService;

    @PostMapping("/addcompany")
    public ResponseEntity<Company> addNewCompany(@Valid @RequestBody Company company ){
        Company savedCompany = companyService.addNewCompany(company);
        return ResponseEntity.ok(savedCompany);
    }

    @PutMapping("/updatecompany/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable int id,@Valid @RequestBody Company company){

        Company savedCompany = companyService.updateCompany(id,company);
        return ResponseEntity.ok(savedCompany);
    }

    @GetMapping("/getcompany/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable int id) {
        Company company = companyService.getCompanyById(id).orElseThrow();
        return ResponseEntity.ok(company);
    }
    @DeleteMapping("/deletecompany/{id}")
    public void deleteCompany(@PathVariable int id ){
        companyService.deleteCompany(id);
    }

    @GetMapping("/getallcompanies")
    public List<Company> getAllCompanies() {
        return companyService.findAllCompanies();
    }

    @GetMapping("/productsratings/{id}")
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

    @GetMapping("/countservices/{id}")
    public ResponseEntity<Integer> countservices(@PathVariable int id){
        return ResponseEntity.ok(serviceService.findServicesNumber(id));
    }

    @GetMapping("/appointmentsperservice/{id}")
    public ResponseEntity<List<Map<String,Integer>>> appointmentsPerService(@PathVariable int id){
        return ResponseEntity.ok(serviceService.nbOfAppointmentsPerService(id));
    }

    @GetMapping("/servicesratings/{id}")
    public ResponseEntity<List<Map<String,Integer>>> ratingsOfServiceByName(@PathVariable int id){
        return ResponseEntity.ok(serviceService.ratingsOfServices(id));
    }

    @GetMapping("/bookedinsights/{id}")
    public ResponseEntity<List<Boolean>> BookedAppointmentsInsights(@PathVariable int id){
        return ResponseEntity.ok(appointmentService.bookingInsights(id));
    }

    @GetMapping("/bookedappointmentsperdate/{id}")
    public ResponseEntity<List<Map<String, Object>>> getBookedAppointmentsPerDate(@PathVariable int id){
        return ResponseEntity.ok(appointmentService.findBookedPerDate(id));
    }

    @GetMapping("/percentageofbookings/{id}")
    public ResponseEntity<Double> getPercentageOfBooking(@PathVariable int id){
        return ResponseEntity.ok(appointmentService.findPercentageBookedAppointments(id));
    }

    @GetMapping("/nbofdifferentusers/{id}")
    public ResponseEntity<Integer> getNumberOfDifferentUsers(@PathVariable int id){
        return ResponseEntity.ok(appointmentService.findNumberOfDifferentUsers(id));
    }


    @GetMapping("/percentageofattendance/{id}")
    public ResponseEntity<Double> getPercentageOfAttendance(@PathVariable int id){
        return ResponseEntity.ok(appointmentService.findAttendedNumberOfUsers(id));
    }

    @GetMapping("/getnumberofpets/{id}")
    public ResponseEntity<Integer> getNumberOfPetsForUser(@PathVariable int id){
        return ResponseEntity.ok(petService.getNumberOfPets(id));
    }

    @GetMapping("/getnumberofposts/{id}")
    public ResponseEntity<Integer> getNumberOfPostsForUser(@PathVariable int id){
        return ResponseEntity.ok(postService.getNumberOfPostsForUser(id));
    }

    @GetMapping("/getpurchasesforuser/{id}")
    public ResponseEntity<List<Map<String,Integer>>> getPurchases(@PathVariable int id){
        return ResponseEntity.ok(orderService.getPurchasesByDate(id));
    }

    @GetMapping("/bookedappointmentsbytype/{id}")
    public ResponseEntity<List<Map<String, Object>>> getBookedAppointmentsTypeForUser(@PathVariable int id) {
        return ResponseEntity.ok(appointmentService.findBookedAppointmentsType(id));
    }

}
