package com.company.pawpet.Service;


import com.company.pawpet.Model.Company;
import com.company.pawpet.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public Company addNewCompany(Company company){
        return companyRepository.save(company);
    }

    public Company updateCompany(int companyId,Company company){
        Optional<Company> existingCompany = companyRepository.findById(companyId);

        if (existingCompany.isEmpty()) {
            throw new RuntimeException("Company with ID " + companyId + " not found.");
        }

        Company companyToUpdate = existingCompany.get();

        companyToUpdate.setCompanyName(company.getCompanyName());
        companyToUpdate.setCompanyAddress(company.getCompanyAddress());

        return companyRepository.save(companyToUpdate);
    }
    public Optional<Company> getCompanyById(int id) {
        return companyRepository.findById(id);
    }
    public void deleteCompany(int companyId){
        companyRepository.deleteById(companyId);
    }

    public List<Company> findAllCompanies(){
        return companyRepository.findAll();
    }

}
