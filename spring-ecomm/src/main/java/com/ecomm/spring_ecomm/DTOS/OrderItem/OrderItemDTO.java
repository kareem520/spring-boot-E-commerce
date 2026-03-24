package com.ecomm.spring_ecomm.DTOS.OrderItem;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private String id;
    private String productId;
    private String productName;

    private double price;
    private double discountPercentage;
    private double orderProductPrice;

    private int quantity;

    private double subTotal;
}
