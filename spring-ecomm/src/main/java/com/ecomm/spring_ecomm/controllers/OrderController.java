package com.ecomm.spring_ecomm.controllers;


import com.ecomm.spring_ecomm.DTOS.Order.OrderDTO;
import com.ecomm.spring_ecomm.configurations.AppConstants;
import com.ecomm.spring_ecomm.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class OrderController {


    @Autowired
    OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders(){
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<OrderDTO> placeOrder(
            @RequestParam(name = "customerEmail", defaultValue = AppConstants.CUSTOMER_EMAIL, required = false) String customerEmail,
            @RequestParam(name = "customerName", defaultValue = AppConstants.CUSTOMER_NAME, required = false) String customerName,
            @RequestParam(name = "customerAddress", defaultValue = AppConstants.CUSTOMER_ADDRESS, required = false) String customerAddress,
            @RequestParam(name = "customerPhone", defaultValue = AppConstants.CUSTOMER_PHONE, required = false) String customerPhone
            )
    {

        OrderDTO orderDTO = orderService.placeMyOrder(customerEmail,customerAddress,customerName,customerPhone);
        return new  ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
