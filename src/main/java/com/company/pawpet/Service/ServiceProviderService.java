package com.company.pawpet.Service;

import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.*;
import com.company.pawpet.Repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ServiceProviderService {

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    CompanyService companyService;

    @Autowired
    AppointmentService appointmentService;

    private ServiceService serviceService;

    final BCryptPasswordEncoder passwordEncoder;

    public ServiceProviderService(BCryptPasswordEncoder passwordEncoder,@Lazy ServiceService serviceService) {
        this.passwordEncoder = passwordEncoder;
        this.serviceService = serviceService;

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

    public ServiceProvider updateServiceProvider(int ppId, int companyId, ServiceProvider sp) {
        ServiceProvider existingSp = serviceProviderRepository.findById(ppId).orElseThrow();
        Company company = companyService.getCompanyById(companyId).orElseThrow();

        ServiceProvider SPToUpdate = existingSp;

        SPToUpdate.setFirstname(sp.getFirstname());
        SPToUpdate.setLastname(sp.getLastname());
        SPToUpdate.setBirthDate(sp.getBirthDate());
        SPToUpdate.setGender(sp.getGender());
        SPToUpdate.setPhone(sp.getPhone());
        SPToUpdate.setImage(sp.getImage());
        SPToUpdate.setAddress(sp.getAddress());
        SPToUpdate.setCompany(company);
        SPToUpdate.setSpecialization(sp.getSpecialization());

        return serviceProviderRepository.save(SPToUpdate);
    }

    public void setAvailability(int spId, Map<String,String> newAvailability){
        ServiceProvider sp = serviceProviderRepository.findById(spId).orElseThrow();
        Map<String, String> oldAvailability = sp.getAvailableDays();
        List<ServiceModel> services = serviceService.getAllServicesBySP(spId);
        List<Appointment> appointments = new ArrayList<>();
        for(ServiceModel service : services){
            appointments.addAll(appointmentService.findAppointmentsByService(service.getServiceId()));
        }
        for (Map.Entry<String, String> entry : oldAvailability.entrySet()) {
            String day = entry.getKey();
            String oldTimeRange = entry.getValue();
            String newTimeRange = newAvailability.get(day);
            if (newTimeRange != null && !newTimeRange.trim().replaceAll("\\s+", "").equalsIgnoreCase(oldTimeRange.trim().replaceAll("\\s+", ""))) {
                for (Appointment appointment : appointments) {
                    if (appointment.getDayOfWeek().toString().equalsIgnoreCase(day)) {
                        appointmentService.DeleteAppointment(appointment.getAppointmentId());
                    }
                }
            }
        }
        sp.setAvailableDays(newAvailability);
        serviceProviderRepository.save(sp);
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

    public Map<String,String> getAvailability(int spId){
        ServiceProvider serviceProvider = serviceProviderRepository.findById(spId).orElseThrow();
            return serviceProvider.getAvailableDays();
    }

    public  ServiceProvider getSPById(int spId) {
        return serviceProviderRepository.findById(spId).orElseThrow();
    }

}
