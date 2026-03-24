package com.ecomm.spring_ecomm.DTOS.cart;

import com.ecomm.spring_ecomm.DTOS.cartItem.CartItemDto;
import com.ecomm.spring_ecomm.models.CartItem;
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
