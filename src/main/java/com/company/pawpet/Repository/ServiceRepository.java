package com.company.pawpet.Repository;

import com.company.pawpet.Model.Product;
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

    @Query(value = "SELECT * FROM services WHERE service_provider_id = :providerId", nativeQuery = true)
    List<ServiceModel> findServicesByProviderId(@Param("providerId") int providerId);

    @Query(value = """
  SELECT mscategory_key
  FROM category_mscategory
  WHERE category_category_id IN (
    SELECT category_id
    FROM categories 
    WHERE type = 'SERVICE'
  )
""", nativeQuery = true)
    List<String> findMSCategoryKeysForService();

    @Query(value = """
  SELECT *
  FROM services
  WHERE category IN (
      SELECT category_id
      FROM categories 
      WHERE category_id IN (
        SELECT category_category_id
        FROM category_mscategory
        WHERE mscategory_key = :type
      )
    )
""", nativeQuery = true)
    List<ServiceModel> findServiceByCategory(@Param("type") String type);

    @Query(value = "SELECT COUNT(*) from services where service_provider_id = :id",nativeQuery = true)
    Integer countServices(@Param("id") int id);
}
