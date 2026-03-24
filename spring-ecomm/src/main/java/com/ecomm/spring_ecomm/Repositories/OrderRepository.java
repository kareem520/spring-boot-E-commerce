package com.ecomm.spring_ecomm.Repositories;

import com.ecomm.spring_ecomm.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("SELECT c FROM Order c LEFT JOIN FETCH c.orderItems")
    List<Order> findAllWithOrderItems();
}
