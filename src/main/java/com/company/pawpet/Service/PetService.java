package com.company.pawpet.Service;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Category;
import com.company.pawpet.Model.Pet;
import com.company.pawpet.Repository.CategoryRepository;
import com.company.pawpet.Repository.PetRepository;
import com.company.pawpet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    UserRepository appUserRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    public Pet addNewPet(Pet pet, int userId,int categoryId){

        Optional<Category> category = categoryService.findById(categoryId);


        Pet newPet = new Pet();
        newPet.setPetName(pet.getPetName());
        newPet.setCreatedAt(LocalDateTime.now());
        newPet.setImage(pet.getImage());
        newPet.setGender(pet.getGender());
        newPet.setStatus(pet.getStatus());
        newPet.setWeight(pet.getWeight());
        newPet.setAge(pet.getAge());
        newPet.setVaccinationRecord(pet.getVaccinationRecord());
        newPet.setMedicalConditions(pet.getMedicalConditions());
        newPet.setAllergies(pet.getAllergies());
        newPet.setPetCategory(pet.getPetCategory());
        newPet.setDietaryPreferences(pet.getDietaryPreferences());
        newPet.setLastVetVisit(pet.getLastVetVisit());
        newPet.setPetCategory(category.orElse(null));
        Optional<AppUser> appUser = appUserRepository.findById(userId);
        if (appUser.isPresent()) {
            newPet.setAppUser(appUser.get());
        } else {
            throw new RuntimeException("User not found");
        }
        return petRepository.save(newPet);
    }
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Optional<Pet> getPetById(int id) {
        return petRepository.findById(id);
    }

    public void deletePet(int petId){
        petRepository.deleteById(petId);
    }

    public List<Pet> findPetByGender(String gender){
        return petRepository.findPetByGender(gender);
    }

    public List<Pet> findPetByStatus(String status){
        return petRepository.findPetByStatus(status);
    }

    public Pet updatePet( int id,int categoryId,Pet pet ){
        Optional<Pet> existingPet = petRepository.findById(id);

        if (existingPet.isEmpty()) {
            throw new RuntimeException("Pet with ID " + id + " not found.");
        }
        Optional<Category> category = categoryService.findById(categoryId);

        Pet petToUpdate = existingPet.get();

        petToUpdate.setPetName(pet.getPetName());
        petToUpdate.setGender(pet.getGender());
        petToUpdate.setStatus(pet.getStatus());
        petToUpdate.setWeight(pet.getWeight());
        petToUpdate.setAge(pet.getAge());
        petToUpdate.setVaccinationRecord(pet.getVaccinationRecord());
        petToUpdate.setMedicalConditions(pet.getMedicalConditions());
        petToUpdate.setAllergies(pet.getAllergies());
        petToUpdate.setDietaryPreferences(pet.getDietaryPreferences());
        petToUpdate.setLastVetVisit(pet.getLastVetVisit());
        petToUpdate.setPetCategory(category.orElse(null));


        return petRepository.save(petToUpdate);
    }

    public List<Pet> getAllPets(int id) {
        return petRepository.findPetsByUserId(id);}



}
