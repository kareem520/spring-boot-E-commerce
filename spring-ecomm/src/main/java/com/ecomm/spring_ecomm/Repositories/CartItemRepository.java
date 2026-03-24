package com.ecomm.spring_ecomm.Repositories;

import com.ecomm.spring_ecomm.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByProduct_idAndCart_id(String product_id,String cart_id);
    void  deleteByProduct_idAndCart_id(String product_id,String cart_id);
}
