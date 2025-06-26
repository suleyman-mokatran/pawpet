package com.company.pawpet.Controller;

import com.company.pawpet.Model.*;
import com.company.pawpet.Repository.UserRepository;
import com.company.pawpet.Security.JwtUtil;
import com.company.pawpet.Service.AppUserService;
import com.company.pawpet.Service.AppointmentService;
import com.company.pawpet.Service.CustomUserDetailsService;
import com.company.pawpet.Service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




import static com.company.pawpet.Enum.Role.USER;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private AppUserService userService;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private NotificationService notificationService;

    String token;
    private AppUser user;
    private Category category;
    private Pet pet;
    private Doctor doctor;
    private Appointment appointment;
    private Notification notification;

    @BeforeEach
    void setup(){
        user = new AppUser();
        user.setAppUserId(1);
        user.setUsername("fakeuser@gmail.com");
        user.setPassword(new BCryptPasswordEncoder().encode("pass123"));
        user.setRole(USER);

        category = new Category();
        category.setCategoryId(1);
        category.setType("PET");

        pet = new Pet();
        pet.setPetId(1);
        pet.setPetName("fakePet");
        pet.setStatus("Adopted");
        pet.setAge(10);
        pet.setWeight(10);
        pet.setGender("male");
        pet.setPetCategory(category);

        doctor = new Doctor();
        doctor.setAppUserId(2);
        doctor.setSpecialization("Cat");
        doctor.setUsername("fakedoctor@gmail.com");
        doctor.setPassword(new BCryptPasswordEncoder().encode("pass123"));

        appointment = new Appointment();
        appointment.setType("VET");
        appointment.setBooked(false);
        appointment.setSelectedDate(LocalDate.of(2025,07,10));
        appointment.setPrice(10);
        appointment.setStartTime("10:00 AM");
        appointment.setEndTime("11:00 AM");
        appointment.setAppointmentId(1);

        Mockito.when(customUserDetailsService.loadUserByUsername("fakeuser@gmail.com"))
                .thenReturn(user);

         token = jwtUtil.generateToken(user);

        Mockito.when(userService.getUserById(user.getAppUserId()))
                .thenReturn(Optional.of(user));

        Mockito.when(userService.getUserById(user.getAppUserId()))
                .thenReturn(Optional.of(user));

    }

    @Test
    void getUserProfile_Success() throws Exception {
        mockMvc.perform(get("/user/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_Success() throws Exception {

        mockMvc.perform(get("/user/getuser/" + user.getAppUserId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void getUserPets_Success() throws Exception {

        pet.setAppUser(user);

        mockMvc.perform(get("/user/getpets/" + user.getAppUserId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

    }

    @Test
    void addNewPet_Success() throws Exception {
        mockMvc.perform(post("/user/addpet/" + user.getAppUserId() + "/" + category.getCategoryId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(pet)))
                .andExpect(status().isOk());
    }

    @Test
    void getUserPetsByCategory_Success() throws Exception {

       pet.setAppUser(user);

        mockMvc.perform(get("/user/getpetbycategory/" + user.getAppUserId() + "/" + category.getType())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void bookVetAppointment_Success() throws Exception {

        Mockito.when(appointmentService.getAppointment(appointment.getAppointmentId()))
                .thenReturn(appointment);

        Mockito.when(notificationService.addNewMessageNotification(doctor.getAppUserId(),"Booked"))
                .thenReturn(notification);


        mockMvc.perform(put("/user/confirmbooking/" + user.getAppUserId() + "/" + doctor.getAppUserId() + "/" + pet.getPetId() + "/" + appointment.getAppointmentId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());



    }



}