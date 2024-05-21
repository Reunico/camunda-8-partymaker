package com.example.camunda8.service;

import com.example.camunda8.model.Order;

import java.time.LocalDate;
import java.util.Date;

public interface OrderService {

    Order persistOrder(
            Order order,
                        String description,
                        String contractor,
                        LocalDate orderDate,
                        String customerName,
                        String title,
                        Long amount
    );
}
