package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.cart.CartDTO;
import com.ecomm.spring_ecomm.models.CartItem;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    CartDTO getCartByEmail(String email);
    CartDTO addProductToCart(String productId, int quantity);
    List<CartDTO> getCarts();
    @Transactional
    CartDTO updateProductQuantityInCart(String productId, Integer quantity);
    @Transactional
    String deleteProductFromCart(String productId);
}
