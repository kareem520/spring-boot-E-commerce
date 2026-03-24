package com.ecomm.spring_ecomm.DTOS.cartItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private String id;
    private String productId;
    private String productName;

    private double price;
    private double discountPercentage;
    private double specialPrice;

    private int quantity;

    private double subTotal;
}
