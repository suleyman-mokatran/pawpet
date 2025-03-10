package com.company.pawpet.Service;

import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.ProductProvider;
import com.company.pawpet.Repository.ProductProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductProviderService {

    @Autowired
    ProductProviderRepository productProviderRepository;

    final BCryptPasswordEncoder passwordEncoder;

    public ProductProviderService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ProductProvider addNewPP(ProductProvider pp){


        ProductProvider newPp =new ProductProvider();

        newPp.setUsername(pp.getUsername());
        newPp.setPassword(passwordEncoder.encode(pp.getPassword()));
        newPp.setRole(Role.PP);
        newPp.setAddress(pp.getAddress());
        newPp.setFirstname(pp.getFirstname());
        newPp.setLastname(pp.getLastname());
        newPp.setPhone(pp.getPhone());
        newPp.setGender(pp.getGender());
        newPp.setBirthDate(pp.getBirthDate());
        newPp.setImage(pp.getImage());

        return productProviderRepository.save(newPp);
    }

    public void deleteProductProvider(int productProviderId){
        productProviderRepository.deleteById(productProviderId);
    }

    public List<ProductProvider> findAllProductProvidersByCompanyId(int companyId){
        return productProviderRepository.findProductProviderByCompany(companyId);
    }

    public ProductProvider updateProductProvider(int ppId, ProductProvider pp) {
        Optional<ProductProvider> existingPp = productProviderRepository.findById(ppId);

        if (existingPp.isEmpty()) {
            throw new RuntimeException("PP with ID " + ppId + " not found.");
        }

        ProductProvider PPToUpdate = existingPp.get();

        PPToUpdate.setFirstname(pp.getFirstname());
        PPToUpdate.setLastname(pp.getLastname());
        PPToUpdate.setBirthDate(pp.getBirthDate());
        PPToUpdate.setGender(pp.getGender());
        PPToUpdate.setUsername(pp.getUsername());
        PPToUpdate.setPhone(pp.getPhone());
        PPToUpdate.setAddress(pp.getAddress());

        return productProviderRepository.save(PPToUpdate);
    }
    public  Optional<ProductProvider> getPPById(int ppId) {
        return productProviderRepository.findById(ppId);
    }

    public List<ProductProvider> getAllProductProviders(){
        return productProviderRepository.findByRole(Role.PP);
    }

    public ProductProvider findByUsername(String username) {
        return productProviderRepository.findByUsername(username);
    }
}
