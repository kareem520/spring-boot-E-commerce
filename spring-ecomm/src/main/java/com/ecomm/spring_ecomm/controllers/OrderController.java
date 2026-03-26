package com.ecomm.spring_ecomm.controllers;


import com.ecomm.spring_ecomm.DTOS.Order.OrderDTO;
import com.ecomm.spring_ecomm.configurations.AppConstants;
import com.ecomm.spring_ecomm.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@Tag(name = "orders", description = "Order API")
public class OrderController {


    @Autowired
    OrderService orderService;


    // ADMIN
    @Operation(summary = "Get all orders from all customers (admin only)")
    @GetMapping("/admin/orders")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public ResponseEntity<List<OrderDTO>> getAllOrdersForAllCustomers() {
        return new ResponseEntity<>(orderService.getAllOrdersForAllCustomers(), HttpStatus.OK);
    }

    // USER
    @Operation(summary = "Get the orders of the currently authenticated user")
    @GetMapping("/user/orders")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<OrderDTO>> getMyOrders() {
        return new ResponseEntity<>(orderService.getMyOrders(), HttpStatus.OK);
    }

    @Operation(summary = "Place a new order for the currently authenticated user with optional customer details")
    @PostMapping("/user/order")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<OrderDTO> placeOrder(
            @RequestParam(name = "customerEmail", defaultValue = AppConstants.CUSTOMER_EMAIL, required = false) String customerEmail,
            @RequestParam(name = "customerName", defaultValue = AppConstants.CUSTOMER_NAME, required = false) String customerName,
            @RequestParam(name = "customerAddress", defaultValue = AppConstants.CUSTOMER_ADDRESS, required = false) String customerAddress,
            @RequestParam(name = "customerPhone", defaultValue = AppConstants.CUSTOMER_PHONE, required = false) String customerPhone
    ) {
        OrderDTO orderDTO = orderService.placeMyOrder(customerEmail, customerAddress, customerName, customerPhone);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
