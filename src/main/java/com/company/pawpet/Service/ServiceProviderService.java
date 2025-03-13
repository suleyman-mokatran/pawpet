package com.company.pawpet.Service;

import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.ServiceProvider;
import com.company.pawpet.Repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceProviderService {

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    final BCryptPasswordEncoder passwordEncoder;

    public ServiceProviderService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ServiceProvider addNewSP(ServiceProvider sp){

        ServiceProvider newSp =new ServiceProvider();

        newSp.setUsername(sp.getUsername());
        newSp.setPassword(passwordEncoder.encode(sp.getPassword()));
        newSp.setRole(Role.SP);
        newSp.setAddress(sp.getAddress());
        newSp.setFirstname(sp.getFirstname());
        newSp.setLastname(sp.getLastname());
        newSp.setPhone(sp.getPhone());
        newSp.setGender(sp.getGender());
        newSp.setBirthDate(sp.getBirthDate());
        newSp.setImage(sp.getImage());
        newSp.setCompany(sp.getCompany());

        return serviceProviderRepository.save(newSp);
    }

    public void deleteServiceProvider(int serviceProviderId){
        serviceProviderRepository.deleteById(serviceProviderId);
    }

    public List<ServiceProvider> findAllServiceProvidersByCompanyId(int companyId){
        return serviceProviderRepository.findServiceProviderByCompany(companyId);
    }

    public ServiceProvider updateServiceProvider(int spId, ServiceProvider sp) {
        Optional<ServiceProvider> existingSp = serviceProviderRepository.findById(spId);

        if (existingSp.isEmpty()) {
            throw new RuntimeException("SP with ID " + spId + " not found.");
        }

        ServiceProvider SPToUpdate = existingSp.get();

        SPToUpdate.setFirstname(sp.getFirstname());
        SPToUpdate.setLastname(sp.getLastname());
        SPToUpdate.setBirthDate(sp.getBirthDate());
        SPToUpdate.setGender(sp.getGender());
        SPToUpdate.setUsername(sp.getUsername());
        SPToUpdate.setPhone(sp.getPhone());
        SPToUpdate.setAddress(sp.getAddress());
        SPToUpdate.setCompany(sp.getCompany());
        SPToUpdate.setImage(sp.getImage());

        return serviceProviderRepository.save(SPToUpdate);
    }

    public Optional<ServiceProvider> getServiceProviderById(int ServiceProviderId){
        return serviceProviderRepository.findById(ServiceProviderId);
    }
    public List<ServiceProvider> getAllServiceProviders(){
        return serviceProviderRepository.findByRole(Role.SP);
    }

    public ServiceProvider findByUsername(String username) {
        return serviceProviderRepository.findByUsername(username);
    }
}
