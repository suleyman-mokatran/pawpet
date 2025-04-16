package com.company.pawpet.Service;

import com.company.pawpet.Model.*;
import com.company.pawpet.Repository.OrderRepository;
import com.company.pawpet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    UserRepository appUserRepository;

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

    public void markOrder(int orderId) {
        Order updatedOrder = orderRepository.findById(orderId).orElseThrow();
        List<OrderItem> orderItems = updatedOrder.getOrderItemList();

        boolean allItemsDone = true;
        for (OrderItem o : orderItems) {
            if (!o.isDone()) {
                allItemsDone = false;
                break;
            }
        }
        updatedOrder.setDone(allItemsDone);
        orderRepository.save(updatedOrder);
    }

    public List<Order> getUserOrders(int userId){
        return orderRepository.findOrdersByUserId(userId);
    }
}