package com.company.pawpet.Service;

import com.company.pawpet.Model.*;
import com.company.pawpet.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ServiceProviderService serviceProviderService;

    @Autowired
    ReviewService reviewService;

    public ServiceModel addNewService(int categoryId,int spId,ServiceModel service) {
        ServiceModel newService = new ServiceModel();
        Category category = categoryService.findById(categoryId);
        ServiceProvider sp = serviceProviderService.getSPById(spId);

        newService.setServiceCategory(category);
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
    public List<ServiceModel> getAllServices(String category) {
        return serviceRepository.findServiceByCategory(category);
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

    public List<Map<String, String>> getServicesCategoriesBySp(int id) {
        List<ServiceModel> services = serviceRepository.findServicesByProviderId(id);
        List<Map<String, String>> servicesCategories = new ArrayList<>();

        for (ServiceModel service : services) {
            Map<String, String> categoryMap = service.getServiceCategory().getMSCategory();
            servicesCategories.add(categoryMap);
        }

        return servicesCategories;
    }

    public List<Map<String, Integer>> ratingsOfServices(int id) {
        List<ServiceModel> services = serviceRepository.findServicesByProviderId(id);
        List<Map<String, Integer>> ratedServices = new ArrayList<>();

        for (ServiceModel s : services) {
            int rate = reviewService.serviceRatingAverage(s.getServiceId());
            String name = s.getName();
            ratedServices.add(Map.of(name, rate));
        }

        return ratedServices;
    }

    public List<Map<String, Integer>> nbOfAppointmentsPerService(int id) {
        List<ServiceModel> services = serviceRepository.findServicesByProviderId(id);
        List<Map<String, Integer>> servicesAppointments = new ArrayList<>();

        for (ServiceModel s : services) {
            int count = 0;
            if (s.getAppointmentList() != null && !s.getAppointmentList().isEmpty()) {
                for (Appointment a : s.getAppointmentList()) {
                    if (a.isBooked()) {
                        count++;
                    }
                }
            }
            servicesAppointments.add(Map.of(s.getName(), count));
        }

        return servicesAppointments;
    }


    public List<String> findServicesByCategory(){
        return serviceRepository.findMSCategoryKeysForService();
    }

    public int findServicesNumber(int id){
        return serviceRepository.countServices(id);
    }



}
