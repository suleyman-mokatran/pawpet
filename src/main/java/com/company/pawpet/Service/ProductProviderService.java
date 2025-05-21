package com.company.pawpet.Service;

import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.Company;
import com.company.pawpet.Model.ProductProvider;
import com.company.pawpet.Repository.ProductProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductProviderService {

    @Autowired
    ProductProviderRepository productProviderRepository;

    @Autowired
    CompanyService companyService;

    final BCryptPasswordEncoder passwordEncoder;

    public ProductProviderService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ProductProvider addNewPP(ProductProvider pp,int id){
    Company company = companyService.getCompanyById(id).orElseThrow();

        ProductProvider newPp =new ProductProvider();

        newPp.setUsername(pp.getUsername());
        newPp.setPassword(passwordEncoder.encode(pp.getPassword()));
        newPp.setRole(Role.PP);
        newPp.setCompany(company);

        return productProviderRepository.save(newPp);
    }

    public void deleteProductProvider(int productProviderId){
        productProviderRepository.deleteById(productProviderId);
    }

    public List<ProductProvider> findAllProductProvidersByCompanyId(int companyId){
        return productProviderRepository.findProductProviderByCompany(companyId);
    }

    public ProductProvider updateProductProvider(int ppId,ProductProvider pp) {
        ProductProvider existingPp = productProviderRepository.findById(ppId).orElseThrow();

        ProductProvider PPToUpdate = existingPp;

        PPToUpdate.setFirstname(pp.getFirstname());
        PPToUpdate.setLastname(pp.getLastname());
        PPToUpdate.setBirthDate(pp.getBirthDate());
        PPToUpdate.setGender(pp.getGender());
        PPToUpdate.setImage(pp.getImage());
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
