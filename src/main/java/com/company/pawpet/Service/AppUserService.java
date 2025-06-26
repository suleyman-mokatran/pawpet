package com.company.pawpet.Service;

import aj.org.objectweb.asm.commons.Remapper;
import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Order;
import com.company.pawpet.Model.Pet;
import com.company.pawpet.Repository.PetRepository;
import com.company.pawpet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService{

    @Autowired
    UserRepository appUserRepository;

    @Autowired
    PetRepository petRepository;

    final BCryptPasswordEncoder passwordEncoder;

    public AppUserService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser addNewUser(AppUser appUser){

        AppUser newUser =new AppUser();

        newUser.setUsername(appUser.getUsername());
        newUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        newUser.setRole(Role.USER);
        newUser.setAddress(appUser.getAddress());
        newUser.setFirstname(appUser.getFirstname());
        newUser.setLastname(appUser.getLastname());
        newUser.setPhone(appUser.getPhone());
        newUser.setGender(appUser.getGender());
        newUser.setBirthDate(appUser.getBirthDate());
        newUser.setImage(appUser.getImage());

        return appUserRepository.save(newUser);
    }


    public void deleteUser(int userId){
        appUserRepository.deleteById(userId);
    }

    public AppUser updateUser(int userId, AppUser user) {
        Optional<AppUser> existingUser = appUserRepository.findById(userId);

        if (existingUser.isEmpty()) {
            throw new RuntimeException("User with ID " + userId + " not found.");
        }

        AppUser userToUpdate = existingUser.get();

        userToUpdate.setFirstname(user.getFirstname());
        userToUpdate.setLastname(user.getLastname());
        userToUpdate.setBirthDate(user.getBirthDate());
        userToUpdate.setGender(user.getGender());
        userToUpdate.setPhone(user.getPhone());
        userToUpdate.setAddress(user.getAddress());
        userToUpdate.setImage(user.getImage());

        return appUserRepository.save(userToUpdate);
    }

    public List<AppUser> getAllUsers(){
        return appUserRepository.findByRole(Role.USER);
    }

    public Optional<AppUser> getUserById(int id) {
        return appUserRepository.findById(id);
    }

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

   public int getTotalUsers(){
        return appUserRepository.numberOfUsers();
   }

    public int getTotalSps(){
        return appUserRepository.numberOfSps();
    }

    public int getTotalPps(){
        return appUserRepository.numberOfPps();
    }

    public int getTotalDoctors(){
        return appUserRepository.numberOfDoctors();
    }

}
