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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
public class Cart extends BaseEntity {


   private double totalPrice;

    @OneToMany(mappedBy= "cart")
    private List<CartItem> items = new ArrayList<>();

    @OneToOne
    @JoinColumn(name= "user_id")
    private User user;

}
