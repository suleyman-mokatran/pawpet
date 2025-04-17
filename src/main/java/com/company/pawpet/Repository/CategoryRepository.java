package com.company.pawpet.Repository;

import com.company.pawpet.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {


    @Query(value = "SELECT cm.category_category_id, cm.mscategory, cm.mscategory_key " +
            "FROM category_mscategory cm " +
            "JOIN categories c ON cm.category_category_id = c.category_id " +
            "WHERE c.type = 'pet'",
            nativeQuery = true)
    List<Map<String,String>> findAllPetCategories();


    @Query(value = "SELECT cm.category_category_id, cm.mscategory, cm.mscategory_key " +
            "FROM category_mscategory cm " +
            "JOIN categories c ON cm.category_category_id = c.category_id " +
            "WHERE c.type = 'product'",
            nativeQuery = true)
    List<Map<String,String>> findAllProductCategories();

    @Query(value = "SELECT cm.category_category_id, cm.mscategory, cm.mscategory_key " +
            "FROM category_mscategory cm " +
            "JOIN categories c ON cm.category_category_id = c.category_id " +
            "WHERE c.type = 'service'",
            nativeQuery = true)
    List<Map<String,String>> findAllServiceCategories();
}
