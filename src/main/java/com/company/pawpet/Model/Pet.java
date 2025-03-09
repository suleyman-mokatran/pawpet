package com.company.pawpet.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int PetId;

    String PetName;
    String Gender;
    String Status;
    int Weight;
    int Age;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate VaccinationRecord;
    String MedicalConditions;
    String Allergies;
    String DietaryPreferences;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate LastVetVisit;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate NextVetVisit;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime CreatedAt;

    @ManyToOne
    @JoinColumn(name = "PetUserId")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "AdopterId")
    private AppUser Adopter;

    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Category PetCategory;



    public int getPetId() {
        return PetId;
    }

    public void setPetId(int petId) {
        PetId = petId;
    }

    public String getPetName() {
        return PetName;
    }

    public void setPetName(String petName) {
        PetName = petName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public LocalDate getVaccinationRecord() {
        return VaccinationRecord;
    }

    public void setVaccinationRecord(LocalDate vaccinationRecord) {
        VaccinationRecord = vaccinationRecord;
    }

    public LocalDate getLastVetVisit() {
        return LastVetVisit;
    }

    public void setLastVetVisit(LocalDate lastVetVisit) {
        LastVetVisit = lastVetVisit;
    }

    public LocalDate getNextVetVisit() {
        return NextVetVisit;
    }

    public void setNextVetVisit(LocalDate nextVetVisit) {
        NextVetVisit = nextVetVisit;
    }

    public String getMedicalConditions() {
        return MedicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        MedicalConditions = medicalConditions;
    }

    public String getAllergies() {
        return Allergies;
    }

    public void setAllergies(String allergies) {
        Allergies = allergies;
    }

    public String getDietaryPreferences() {
        return DietaryPreferences;
    }

    public void setDietaryPreferences(String dietaryPreferences) {
        DietaryPreferences = dietaryPreferences;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        CreatedAt = createdAt;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppUser getAdopter() {
        return Adopter;
    }

    public void setAdopter(AppUser adopter) {
        Adopter = adopter;
    }

    public Category getPetCategory() {
        return PetCategory;
    }

    public void setPetCategory(Category petCategory) {
        PetCategory = petCategory;
    }


}
