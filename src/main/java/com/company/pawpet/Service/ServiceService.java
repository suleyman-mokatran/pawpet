package com.company.pawpet.Service;

import com.company.pawpet.Model.ServiceModel;
import com.company.pawpet.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    public ServiceModel addNewService(ServiceModel service) {
        return serviceRepository.save(service);
    }

    public ServiceModel updateService(int id, ServiceModel service){
        Optional<ServiceModel> existingService = serviceRepository.findById(id);

        if (existingService.isEmpty()) {
            throw new RuntimeException("Service with ID " + id + " not found.");
        }

        ServiceModel serviceToUpdate = existingService.get();

        serviceToUpdate.setName(service.getName());
        serviceToUpdate.setDescription(service.getDescription());
        serviceToUpdate.setPrice(service.getPrice());

        return serviceRepository.save(serviceToUpdate);
    }
    public List<ServiceModel> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<ServiceModel> getServiceById(int id) {
        return serviceRepository.findById(id);
    }

    public void deleteService(int serviceId){
        serviceRepository.deleteById(serviceId);
    }

    public List<ServiceModel> findServicesByPrice(float price){
        return serviceRepository.findServicesByPrice(price);
    }

    public List<ServiceModel> findServicesByCompany(int id){
        return serviceRepository.findServicesByCompany(id);
    }





}
