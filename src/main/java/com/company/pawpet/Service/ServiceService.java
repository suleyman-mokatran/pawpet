package com.company.pawpet.Service;

import com.company.pawpet.Model.*;
import com.company.pawpet.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ServiceProviderService serviceProviderService;

    public ServiceModel addNewService(int categoryId,int spId,ServiceModel service) {
        ServiceModel newService = new ServiceModel();
        Category category = categoryService.findById(categoryId);
        ServiceProvider sp = serviceProviderService.getSPById(spId).orElseThrow();

        newService.setServiceCategory(category);
        newService.setCompany(sp.getCompany());
        newService.setServiceProvider(sp);
        newService.setName(service.getName());
        newService.setDescription(service.getDescription());
        newService.setPrice(service.getPrice());

        return serviceRepository.save(newService);
    }

    public ServiceModel updateService(int categoryId,int id, ServiceModel service){
        ServiceModel existingService = serviceRepository.findById(id).orElseThrow();
        Category category = categoryService.findById(categoryId);

        ServiceModel serviceToUpdate = existingService;

        serviceToUpdate.setServiceCategory(category);
        serviceToUpdate.setName(service.getName());
        serviceToUpdate.setDescription(service.getDescription());
        serviceToUpdate.setPrice(service.getPrice());

        return serviceRepository.save(serviceToUpdate);
    }
    public List<ServiceModel> getAllServices() {
        return serviceRepository.findAll();
    }

    public ServiceModel getServiceById(int id) {
        return serviceRepository.findById(id).orElseThrow();
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

    public List<ServiceModel> getAllServicesBySP(int id) {
        return serviceRepository.findServicesByProviderId(id);
    }



}
