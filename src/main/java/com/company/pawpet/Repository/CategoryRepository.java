package com.company.pawpet.Repository;

import com.company.pawpet.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    List<Category> findByType(String type);
}
