package com.company.pawpet.Repository;

import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    AppUser findByUsername(String username);

    List<AppUser> findByRole(Role role);

    @Query(value = """
  SELECT distinct app_user_id, firstname, lastname 
  FROM appusers 
  WHERE app_user_id IN (
    SELECT app_user_id 
    FROM orders 
    WHERE order_id IN (
      SELECT order_id 
      FROM orderitems 
      WHERE product_id IN (
        SELECT product_id 
        FROM products 
        WHERE product_provider_id = :providerId
      )
    )
  )
""", nativeQuery = true)
    List<Object[]> findUserIdFirstAndLastNameByProviderId(@Param("providerId") int providerId);

    @Query(value = """
  SELECT sum(total_price)
    FROM orders
    WHERE app_user_id = :userId
  
""", nativeQuery = true)
    int findCountOfOrderItemsByUserId(@Param("userId") int userId);








}
