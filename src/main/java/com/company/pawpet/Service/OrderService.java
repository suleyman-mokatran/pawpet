package com.company.pawpet.Service;

import com.company.pawpet.Model.*;
import com.company.pawpet.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AppUserService appUserService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(int id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(int id) {
        Order newOrder = new Order();
        AppUser appUser = appUserService.getUserById(id).orElseThrow();
        newOrder.setAppUser(appUser);
        newOrder.setTotalPrice(0);
        newOrder.setCreatedAt(LocalDateTime.now());
        newOrder.setOrderItemList(new ArrayList<>());
        return orderRepository.save(newOrder);
    }

    public Order updateOrder(int id,Order order){
        Order existingOrder = orderRepository.findById(id).orElseThrow();
        existingOrder.setOrderItemList(order.getOrderItemList());
        float total = 0;
        for (OrderItem item : existingOrder.getOrderItemList()) {
            total += item.getPrice()*item.getQuantity();
        }
        existingOrder.setTotalPrice(total);
        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }
}