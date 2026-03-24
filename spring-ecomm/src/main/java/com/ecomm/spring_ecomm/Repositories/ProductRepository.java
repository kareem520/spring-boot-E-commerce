package com.ecomm.spring_ecomm.Repositories;

import com.ecomm.spring_ecomm.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,String> {

    Optional<Product> findById(String s);
    Page<Product>findByCategory_IdOrderByPriceAsc(String categoryId, Pageable pageable);
    Page<Product>findByNameLikeIgnoreCase(String keyword, Pageable pageable);
}
