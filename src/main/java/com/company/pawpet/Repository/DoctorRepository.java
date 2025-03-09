package com.company.pawpet.Repository;

import com.company.pawpet.Model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Integer> {



    @Query(value = "SELECT * FROM doctor WHERE specialization = :specialization", nativeQuery = true)
    List<Doctor> findDoctorBySpecialization(@Param("specialization") String specialization);

    Doctor findByUsername(String username);
}
