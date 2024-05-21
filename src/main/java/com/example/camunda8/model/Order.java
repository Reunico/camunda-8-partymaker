package com.example.camunda8.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Entity
@Table(name = "party_order")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    private UUID id;

    @JsonAlias("name.findName")
    private String fullName;
    @JsonAlias("name.title")
    private String title;
    @JsonAlias("finance.amount")
    private BigDecimal amount;

    private String description;
    private String contractor;
    private Date orderDate;

    public Order(){}
}
