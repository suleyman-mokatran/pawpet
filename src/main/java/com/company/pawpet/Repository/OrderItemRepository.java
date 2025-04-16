package com.company.pawpet.Repository;

import com.company.pawpet.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query(value = """
    SELECT oi.*
    FROM orderitems oi
    JOIN products p ON oi.product_id = p.product_id
    WHERE p.product_provider_id = :providerId
    ORDER BY oi.order_id
""", nativeQuery = true)
    List<OrderItem> findOrderItemsByProductProvider(@Param("providerId") int providerId);

    @Query(value = """
    SELECT oi.*
    FROM orderitems oi
    JOIN orders o ON oi.order_id = o.order_id
    WHERE o.order_id = :orderId""",
            nativeQuery = true)
    List<OrderItem> findOrderItemsByOrder(@Param("orderId") int orderId);

}
