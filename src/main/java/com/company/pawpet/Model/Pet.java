package com.company.pawpet.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "pets")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "petId")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int petId;

    @Lob
    private byte[] image;

    @JsonProperty("PetName")
    String PetName;

    String Gender;
    String Status;
    boolean forAdoption;
    int Weight;
    int Age;
    int lostPostId;
    int adoptPostId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ElementCollection
    Map<String,LocalDate> VaccinationRecord;

    @ElementCollection
    private List<String> medicalConditions;

    @ElementCollection
    List<String> Allergies;

    @ElementCollection
    List<String> DietaryPreferences;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate LastVetVisit;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate NextVetVisit;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime CreatedAt;

    @ManyToOne
    @JoinColumn(name = "petUserId")
    @JsonProperty("petUserId")
    private AppUser appUser;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CategoryId")
    @JsonProperty("petCategory")
    private Category PetCategory;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Appointment> appointmentList;


    public Pet() {
    }

    public Pet(byte[] image, String petName, String gender, String status, int weight, int age,
               Map<String,LocalDate> vaccinationRecord, List<String> medicalConditions, List<String> allergies,
               List<String> dietaryPreferences, LocalDate lastVetVisit, LocalDate nextVetVisit, LocalDateTime createdAt) {

        this.image = image;
        this.PetName = petName;
        this.Gender = gender;
        this.Status = status;
        this.Weight = weight;
        this.Age = age;
        this.VaccinationRecord = vaccinationRecord;
        this.medicalConditions = medicalConditions;
        this.Allergies = allergies;
        this.DietaryPreferences = dietaryPreferences;
        this.LastVetVisit = lastVetVisit;
        this.NextVetVisit = nextVetVisit;
        this.CreatedAt = createdAt;
    }

    public int getLostPostId() {
        return lostPostId;
    }

    public void setLostPostId(int lostPostId) {
        this.lostPostId = lostPostId;
    }

    public int getAdoptPostId() {
        return adoptPostId;
    }

    public void setAdoptPostId(int adoptPostId) {
        this.adoptPostId = adoptPostId;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public boolean isForAdoption() {
        return forAdoption;
    }

    public void setForAdoption(boolean forAdoption) {
        this.forAdoption = forAdoption;
    }

    public byte[] getImage() {return image;}

    public void setImage(byte[] image) {this.image = image;}

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        petId = petId;
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

    public Map<String,LocalDate> getVaccinationRecord() {
        return VaccinationRecord;
    }

    public void setVaccinationRecord(Map<String,LocalDate> vaccinationRecord) {
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

    public List<String> getMedicalConditions() {return medicalConditions;}

    public void setMedicalConditions(List<String> medicalConditions) {this.medicalConditions = medicalConditions;}

    public List<String> getAllergies() {
        return Allergies;
    }

    public void setAllergies(List<String> allergies) {
        Allergies = allergies;
    }

    public List<String> getDietaryPreferences() {
        return DietaryPreferences;
    }

    public void setDietaryPreferences(List<String> dietaryPreferences) {
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


    public Category getPetCategory() {
        return PetCategory;
    }

    public void setPetCategory(Category petCategory) {
        PetCategory = petCategory;
    }


}
