package com.ecomm.spring_ecomm.Repositories;

import com.ecomm.spring_ecomm.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
}
