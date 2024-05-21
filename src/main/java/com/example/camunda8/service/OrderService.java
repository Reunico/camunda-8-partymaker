package com.example.camunda8.service;

import com.example.camunda8.model.Order;

import java.util.Date;

public interface OrderService {

    Order persistOrder(
            Order order,
                        String description,
                        String contractor,
                        Date orderDate,
                        String customerName,
                        String title,
                        Long amount
    );
}
