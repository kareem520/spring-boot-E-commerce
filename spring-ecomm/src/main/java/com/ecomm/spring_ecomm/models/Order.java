package com.ecomm.spring_ecomm.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {


    private String customerEmail;
    private String customerAddress;
    private String customerName;
    private String customerPhoneNumber;
    private double totalPrice;

    @OneToMany(mappedBy = "order")
    List<OrderItem> orderItems =  new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
