package com.ecomm.spring_ecomm.controllers;


import com.ecomm.spring_ecomm.DTOS.cart.CartDTO;
import com.ecomm.spring_ecomm.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CartController {



    @Autowired
    CartService cartService;


    @PostMapping("/public/carts/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable  String productId,
                                                    @PathVariable int quantity) {

        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }
    @PutMapping("/public/carts/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> updateQuantityForProductExistsInCart(@PathVariable  String productId,
                                                                         @PathVariable int quantity) {

        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/admin/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return new ResponseEntity<>(cartService.getCarts(),HttpStatus.OK);
    }

    @DeleteMapping("/public/carts/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable String productId) {
        String status = cartService.deleteProductFromCart(productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }


}
