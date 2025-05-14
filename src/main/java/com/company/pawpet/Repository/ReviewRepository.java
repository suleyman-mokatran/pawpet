package com.company.pawpet.Repository;

import com.company.pawpet.Model.Post;
import com.company.pawpet.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ReviewRepository extends JpaRepository<Review,Integer> {

    @Query(value = "SELECT * FROM reviews WHERE doctor = :doctorId", nativeQuery = true)
    List<Review> findByDoctorId(int doctorId);

    @Query(value = "SELECT * FROM reviews WHERE product = :productId", nativeQuery = true)
    List<Review> findByProductId(int productId);

    @Query(value = "SELECT * FROM reviews WHERE service = :serviceId", nativeQuery = true)
    List<Review> findByServiceId(int serviceId);

    @Query(value = "SELECT rating FROM reviews WHERE doctor = :doctorId", nativeQuery = true)
    List<Integer> findRatingByDoctorId(int doctorId);

    @Query(value = "SELECT rating FROM reviews WHERE product = :productId", nativeQuery = true)
    List<Integer> findRatingByProductId(int productId);

    @Query(value = "SELECT rating FROM reviews WHERE service = :serviceId", nativeQuery = true)
    List<Integer> findRatingByServiceId(int serviceId);
}
