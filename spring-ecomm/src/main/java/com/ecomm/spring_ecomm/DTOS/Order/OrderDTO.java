package com.ecomm.spring_ecomm.DTOS.Order;

import com.ecomm.spring_ecomm.DTOS.OrderItem.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private String id;
    private List<OrderItemDTO> orderItems;
    private String createdBy;
    private LocalDateTime createdDate;

    private String customerEmail;
    private String customerAddress;
    private String customerName;
    private String customerPhoneNumber;
    private double totalPrice;
}
