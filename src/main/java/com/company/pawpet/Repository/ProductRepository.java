package com.company.pawpet.Repository;

import com.company.pawpet.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM products WHERE product_provider_id = :providerId", nativeQuery = true)
    List<Product> findProductsByProviderId(@Param("providerId") int providerId);

    @Query(value = """
  SELECT mscategory_key
  FROM category_mscategory
  WHERE category_category_id = (
    SELECT category_id
    FROM categories 
    WHERE type = 'PRODUCT'
  )
""", nativeQuery = true)
    List<String> findMSCategoryKeysForProduct();

    @Query(value = """
  SELECT *
  FROM products
  WHERE category = (
    SELECT category_id
    FROM categories 
    WHERE category_id = (
    SELECT category_category_id
  FROM category_mscategory
  WHERE mscategory_key = :type
  )
  )
""", nativeQuery = true)
    List<Product> findProductByCategory(@Param("type") String type);

}
