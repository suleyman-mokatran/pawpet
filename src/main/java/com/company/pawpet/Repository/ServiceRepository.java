package com.company.pawpet.Repository;

import com.company.pawpet.Model.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ServiceRepository extends JpaRepository<ServiceModel,Integer> {


    @Query(value = "SELECT * FROM Services WHERE price = :price", nativeQuery = true)
    List<ServiceModel> findServicesByPrice(@Param("price") float price);

    @Query(value = "SELECT * FROM Services WHERE company_id = :companyId", nativeQuery = true)
    List<ServiceModel> findServicesByCompany(@Param("companyId") int companyId);
}
