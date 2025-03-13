package com.company.pawpet.Service;

import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Doctor;
import com.company.pawpet.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    final BCryptPasswordEncoder passwordEncoder;

    public AdminService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser addNewAdmin(AppUser admin){

        AppUser newAdmin =new AppUser();

        newAdmin.setUsername(admin.getUsername());
        newAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        newAdmin.setRole(Role.ADMIN);
        newAdmin.setAddress(admin.getAddress());
        newAdmin.setFirstname(admin.getFirstname());
        newAdmin.setLastname(admin.getLastname());
        newAdmin.setPhone(admin.getPhone());
        newAdmin.setGender(admin.getGender());
        newAdmin.setBirthDate(admin.getBirthDate());
        newAdmin.setImage(admin.getImage());

        return adminRepository.save(newAdmin);
    }

    public void deleteAdmin(int adminId){
        adminRepository.deleteById(adminId);
    }

    public AppUser updateAdmin(int adminId, AppUser admin) {
        Optional<AppUser> existingAdmin = adminRepository.findById(adminId);

        if (existingAdmin.isEmpty()) {
            throw new RuntimeException("Admin with ID " + adminId + " not found.");
        }

        AppUser adminToUpdate = existingAdmin.get();

        adminToUpdate.setFirstname(admin.getFirstname());
        adminToUpdate.setLastname(admin.getLastname());
        adminToUpdate.setBirthDate(admin.getBirthDate());
        adminToUpdate.setGender(admin.getGender());
        adminToUpdate.setPhone(admin.getPhone());
        adminToUpdate.setAddress(admin.getAddress());
        adminToUpdate.setImage(admin.getImage());

        return adminRepository.save(adminToUpdate);
    }

    public List<AppUser> getAllAdmins(){
        return adminRepository.findByRole(Role.ADMIN);
    }
    public Optional<AppUser> getAdminById(int adminId) {
        return adminRepository.findById(adminId);
    }

    public AppUser getUserByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
}
