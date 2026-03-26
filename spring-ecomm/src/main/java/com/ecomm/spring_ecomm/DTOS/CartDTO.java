package com.ecomm.spring_ecomm.DTOS;

import com.ecomm.spring_ecomm.DTOS.cartItem.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private String id;
    private double totalPrice;
    List<CartItemDto> cartItems;
}
