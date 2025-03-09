package com.company.pawpet.Model;



import com.company.pawpet.Enum.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.*;
import java.lang.annotation.Inherited;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity

@Table(name = "appusers")
@Inheritance (strategy =  InheritanceType.JOINED)
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int AppUserId;
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    String firstname;
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    String lastname;

    @Lob
    private byte[] image;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @NotNull(message = "Gender is required")
    String gender;

    @Email
    @Column (unique = true,nullable = false)
    String username;
    @Min(value = 10000000, message = "Phone number must be at least 8 digits")
    @Column (unique = true,nullable = false)
    int phone;

    @Enumerated(EnumType.STRING)
    private Role role;
    String address;
    @Column(nullable = false)
    String password;

    public AppUser() {
    }

    public AppUser(String password, String address, Role role, int phone, String username, String gender, LocalDate birthDate, String lastname, String firstname,byte[] image) {
        this.password = password;
        this.address = address;
        this.role = role;
        this.phone = phone;
        this.username = username;
        this.gender = gender;
        this.birthDate = birthDate;
        this.lastname = lastname;
        this.firstname = firstname;
        this.image = image;
    }

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Appointment> appointmentList;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Pet> petList;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Pet> AdoptedPets;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Order> orderList;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CartId", referencedColumnName = "CartId")
    private Cart cart;

    public int getAppUserId() {
        return AppUserId;
    }

    public void setAppUserId(int appUserId) {
        AppUserId = appUserId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        this.username=username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    public List<Pet> getAdoptedPets() {
        return AdoptedPets;
    }

    public void setAdoptedPets(List<Pet> adoptedPets) {
        AdoptedPets = adoptedPets;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

