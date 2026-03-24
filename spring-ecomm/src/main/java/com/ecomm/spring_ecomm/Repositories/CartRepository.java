package com.ecomm.spring_ecomm.Repositories;

import com.ecomm.spring_ecomm.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser_Email(String email);
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items")
    List<Cart> findAllWithItems();
}
