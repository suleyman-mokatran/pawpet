package com.company.pawpet.Service;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Category;
import com.company.pawpet.Model.Pet;
import com.company.pawpet.Model.Post;
import com.company.pawpet.Repository.CategoryRepository;
import com.company.pawpet.Repository.PetRepository;
import com.company.pawpet.Repository.PostRepository;
import com.company.pawpet.Repository.UserRepository;
import com.company.pawpet.chat.Message;
import com.company.pawpet.chat.MessageRepository;
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

    @Autowired
    PostRepository postRepository;

    @Autowired
    MessageRepository messageRepository;

    public Pet addNewPet(Pet pet, int userId,int categoryId){

        Category category = categoryService.findById(categoryId);


        Pet newPet = new Pet();
        newPet.setPetName(pet.getPetName());
        newPet.setCreatedAt(LocalDateTime.now());
        newPet.setImage(pet.getImage());
        newPet.setGender(pet.getGender());
        newPet.setStatus("Adopted");
        newPet.setWeight(pet.getWeight());
        newPet.setForAdoption(false);
        newPet.setAge(pet.getAge());
        newPet.setVaccinationRecord(pet.getVaccinationRecord());
        newPet.setMedicalConditions(pet.getMedicalConditions());
        newPet.setAllergies(pet.getAllergies());
        newPet.setPetCategory(pet.getPetCategory());
        newPet.setDietaryPreferences(pet.getDietaryPreferences());
        newPet.setLastVetVisit(pet.getLastVetVisit());
        newPet.setPetCategory(category);
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

    public Pet updatePet(int id,int categoryId,Pet pet ){
        Optional<Pet> existingPet = petRepository.findById(id);

        if (existingPet.isEmpty()) {
            throw new RuntimeException("Pet with ID " + id + " not found.");
        }
        Category category = categoryService.findById(categoryId);

        Pet petToUpdate = existingPet.get();

        petToUpdate.setPetName(pet.getPetName());
        petToUpdate.setGender(pet.getGender());
        petToUpdate.setImage(pet.getImage());
        petToUpdate.setWeight(pet.getWeight());
        petToUpdate.setAge(pet.getAge());
        petToUpdate.setVaccinationRecord(pet.getVaccinationRecord());
        petToUpdate.setMedicalConditions(pet.getMedicalConditions());
        petToUpdate.setAllergies(pet.getAllergies());
        petToUpdate.setDietaryPreferences(pet.getDietaryPreferences());
        petToUpdate.setLastVetVisit(pet.getLastVetVisit());
        petToUpdate.setPetCategory(category);


        return petRepository.save(petToUpdate);
    }

    public List<Pet> getAllPets(int id) {
        return petRepository.findPetsByUserId(id);}

    public List<Pet> getFoundLostPes(){
        List<Pet> allPets = petRepository.findAll();
        List<Pet> lostOrFound = new ArrayList<>();
        for(Pet p : allPets){
            if(!p.getStatus().equals("adopted")){
                lostOrFound.add(p);
            }
        }
        return lostOrFound;
    }

    public Pet markAsLost(int id){
        Pet pet = petRepository.findById(id).orElseThrow();
        pet.setStatus("Lost");
        petRepository.save(pet);
        return pet;
    }

    public Pet markAsFound(int id){
        Pet pet = petRepository.findById(id).orElseThrow();
        pet.setStatus("Adopted");
        petRepository.save(pet);
        return pet;
    }

    public Pet forAdoption(int id){
        Pet pet = petRepository.findById(id).orElseThrow();
        pet.setForAdoption(true);
        petRepository.save(pet);
        return pet;
    }

    public Pet cancelForAdoption(int id){
        Pet pet = petRepository.findById(id).orElseThrow();
        messageRepository.deleteMessagesByPetId(id);
        pet.setForAdoption(false);
        petRepository.save(pet);
        return pet;
    }

        public void transferPetFile(int adopterId,int petId){
        AppUser adopter = appUserRepository.findById(adopterId).orElseThrow();
        Pet pet = petRepository.findById(petId).orElseThrow();
        postRepository.deletePostImagesByPetId(petId);
        postRepository.deletePostsByPetId(petId);
        pet.setForAdoption(false);
        pet.setAppUser(adopter);
        petRepository.save(pet);
        }

        public int getNumberOfPets(int id){
        return petRepository.findNumberOfPets(id);
        }
}
