package com.example.camunda8.service;

import com.example.camunda8.model.Order;
import com.example.camunda8.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order persistOrder(Order order, String description, String contractor, LocalDate orderDate, String customerName, String title, Long amount) {
        if (order == null) {order = new Order();}
        order.setDescription(description);
        order.setContractor(contractor);
        order.setOrderDate(orderDate);
        order.setFullName(customerName);
        order.setTitle(title);
        order.setAmount(BigDecimal.valueOf(amount));
        return orderRepository.save(order);
    }
}
