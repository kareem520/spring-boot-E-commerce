package com.ecomm.spring_ecomm.controllers;


import com.ecomm.spring_ecomm.DTOS.CartDTO;
import com.ecomm.spring_ecomm.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "carts", description = "Cart API")
public class CartController {



    @Autowired
    CartService cartService;


    // OWNER USER

    @Operation(summary = "Customers can add products to their carts with a specified quantity")
    @PostMapping("/user/carts/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable String productId,
                                                    @PathVariable int quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Update the quantity of an existing product in the cart (for sellers/customers)")
    @PutMapping("/user/carts/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> updateQuantityForProductExistsInCart(@PathVariable String productId,
                                                                        @PathVariable int quantity) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get the current user's cart with all products and their quantities")
    @GetMapping("/user/cart")
    public ResponseEntity<CartDTO> getMyCart() {
        return new ResponseEntity<>(cartService.getMyCart(), HttpStatus.OK);
    }

    @Operation(summary = "Delete a specific product from the current user's cart")
    @DeleteMapping("/user/carts/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable String productId) {
        String status = cartService.deleteProductFromCart(productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @Operation(summary = "Get all carts in the system (admin only)")
    @GetMapping("/admin/carts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return new ResponseEntity<>(cartService.getCarts(), HttpStatus.OK);
    }

    @Operation(summary = "Get a specific user's cart by email (admin only)")
    @GetMapping("/admin/carts/{email}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CartDTO> getCartsByEmail(@PathVariable String email) {
        return new ResponseEntity<>(cartService.getCartByEmail(email), HttpStatus.OK);
    }



}
