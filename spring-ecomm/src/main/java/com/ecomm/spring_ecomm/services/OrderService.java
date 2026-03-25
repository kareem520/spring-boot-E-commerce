package com.ecomm.spring_ecomm.services;


import com.ecomm.spring_ecomm.DTOS.Order.OrderDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getMyOrders();
    List<OrderDTO> getAllOrdersForAllCustomers();
    @Transactional
    OrderDTO placeMyOrder(String customerEmail, String customerAddress,String name,String customerPhoneNumber);
}
