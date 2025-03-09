package com.company.pawpet.Repository;

import com.company.pawpet.Model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PetRepository extends JpaRepository<Pet,Integer> {

    @Query(value = "SELECT * FROM pets WHERE gender = :gender", nativeQuery = true)
    List<Pet> findPetByGender(@Param("gender") String gender);

    @Query(value = "SELECT * FROM pets WHERE status = :status", nativeQuery = true)
    List<Pet> findPetByStatus(@Param("status") String status);

}
