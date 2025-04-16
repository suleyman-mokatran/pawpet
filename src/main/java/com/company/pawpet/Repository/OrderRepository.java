package com.company.pawpet.Repository;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {


    @Query(value = "SELECT * FROM orders WHERE app_user_id = :userId", nativeQuery = true)
    List<Order> findOrdersByUserId(@Param("userId") int userId);
}
