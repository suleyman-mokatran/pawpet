package com.company.pawpet.Controller;

import com.company.pawpet.Model.*;
import com.company.pawpet.PasswordUpdateRequest;
import com.company.pawpet.Repository.AdminRepository;
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
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



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
    public ResponseEntity<?> addNewPetCategory(@Valid @RequestBody Category category, @RequestParam String categoryName, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Category savedCategory = categoryService.addNewPetCategory(category, categoryName);
        return ResponseEntity.ok(savedCategory);
    }

    @PostMapping("/addproductcategory")
    public ResponseEntity<?> addNewProductCategory(@Valid @RequestBody Category category,
                                                   @RequestParam String categoryName, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        // Setting the name manually and type to PRODUCT
        Category savedCategory = categoryService.addNewProductCategory(category, categoryName);
        return ResponseEntity.ok(savedCategory);
    }
    @GetMapping("/getcategory/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }
    @GetMapping("/getcategories")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @PutMapping("/updatecategory/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category categoryDetails) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDetails.getName());
        return ResponseEntity.ok(updatedCategory);
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

    @PostMapping("/addsp")
    public ResponseEntity<?> addNewSP(@Valid @RequestBody ServiceProvider sp, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        ServiceProvider savedsp = serviceProviderService.addNewSP(sp);
        return ResponseEntity.ok(savedsp);
    }
    @PutMapping("/updatesp/{id}")
    public ResponseEntity<?> updateSP(@PathVariable int id,@Valid @RequestBody ServiceProvider sp, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        ServiceProvider savedsp = serviceProviderService.updateServiceProvider(id,sp);
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
    public ResponseEntity<ServiceProvider> getSpById(@PathVariable int ServiceProviderId) {
        return serviceProviderService.getServiceProviderById(ServiceProviderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
    }

    @Autowired
    ProductProviderService productProviderService;

    @PostMapping("/addpp")
    public ResponseEntity<?> addNewPP(@Valid @RequestBody ProductProvider pp, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        ProductProvider savedpp = productProviderService.addNewPP(pp);
        return ResponseEntity.ok(savedpp);
    }
    @GetMapping("/getpp/{id}")
    public ResponseEntity<ProductProvider> getPPById(@PathVariable int PPid) {
        return productProviderService.getPPById(PPid)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PP not found"));
    }
    @PutMapping("/updatepp/{id}")
    public ResponseEntity<?> updatePP(@PathVariable int id,@Valid @RequestBody ProductProvider pp, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        ProductProvider savedpp = productProviderService.updateProductProvider(id,pp);
        return ResponseEntity.ok(savedpp);
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
    public ResponseEntity<?> addNewCompany(@Valid @RequestBody Company company ,BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Company savedCompany = companyService.addNewCompany(company);
        return ResponseEntity.ok(savedCompany);
    }

    @PutMapping("/updatecompany/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable int id,@Valid @RequestBody Company company, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Company savedCompany = companyService.updateCompany(id,company);
        return ResponseEntity.ok(savedCompany);
    }
    @GetMapping("/getcompany/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable int id) {
        return companyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }
    @DeleteMapping("/deletecompany/{id}")
    public void deleteCompany(@PathVariable int id ){
        companyService.deleteCompany(id);
    }

    @GetMapping("/getallcompanies")
    public List<Company> getAllCompanies() {
        return companyService.findAllCompanies();
    }


}
