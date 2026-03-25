package com.ecomm.spring_ecomm.cart;

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
