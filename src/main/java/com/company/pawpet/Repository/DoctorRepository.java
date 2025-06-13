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

    @Query(value = "SELECT available_days FROM doctor_available_days WHERE available_days_key = :day AND doctor_app_user_id = :doctorId", nativeQuery = true)
    String findByDayAndDoctorId(@Param("day") String day, @Param("doctorId") int doctorId);

    @Query(value = "SELECT d.*, u.*\n" +
            "    FROM Doctor d\n" +
            "    JOIN appusers u ON d.app_user_id = u.app_user_id\n" +
            "    WHERE d.specialization = :specialization", nativeQuery = true)
    List<Doctor> findDoctorBySpecialization(@Param("specialization") String specialization);

    @Query(value = "SELECT specialization FROM doctor", nativeQuery = true)
    List<String> findSpecializations();

    Doctor findByUsername(String username);

    @Query(value = "SELECT d.*, u.*\n" +
            "    FROM Doctor d\n" +
            "    JOIN appusers u ON d.app_user_id = u.app_user_id\n" +
            "    WHERE d.urgent = 1", nativeQuery = true)
    List<Doctor> findDoctorUrgentCases();
}
