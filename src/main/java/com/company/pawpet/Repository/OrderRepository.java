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

    @Query(value = """
    SELECT o.created_at, SUM(o.total_price) AS total
    FROM orders o
    WHERE o.order_id IN (
      SELECT oi.order_id 
      FROM orderitems oi
      WHERE oi.product_id IN (
        SELECT p.product_id 
        FROM products p 
        WHERE p.product_provider_id = :providerId
      )
    )
    GROUP BY o.created_at
    ORDER BY o.created_at
    """, nativeQuery = true)
    List<Object[]> getDailyTotalPriceByProvider(@Param("providerId") int providerId);

}
