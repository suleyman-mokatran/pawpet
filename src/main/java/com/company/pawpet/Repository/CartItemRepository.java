package com.company.pawpet.Repository;

import com.company.pawpet.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
