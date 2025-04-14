package com.company.pawpet.Service;

import com.company.pawpet.Model.CartItem;
import com.company.pawpet.Model.Order;
import com.company.pawpet.Model.OrderItem;
import com.company.pawpet.Model.Product;
import com.company.pawpet.Repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public Optional<OrderItem> getOrderItemById(int id) {
        return orderItemRepository.findById(id);
    }

    public OrderItem saveOrderItem(int orderId,int productId,OrderItem orderItem) {
        OrderItem newOrderItem = new OrderItem();
        Product product = productService.getProductById(productId).orElseThrow();
        Order order = orderService.getOrderById(orderId).orElseThrow();
        newOrderItem.setQuantity(orderItem.getQuantity());
        newOrderItem.setPrice(orderItem.getPrice());
        newOrderItem.setOrder(order);
        newOrderItem.setProduct(product);
        return orderItemRepository.save(newOrderItem);
    }

    public void deleteOrderItem(int id) {
        orderItemRepository.deleteById(id);
    }
}
