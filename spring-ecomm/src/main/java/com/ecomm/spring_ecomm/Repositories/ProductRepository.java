package com.ecomm.spring_ecomm.Repositories;

import com.ecomm.spring_ecomm.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,String> {


    Optional<Product> findByIdAndIsActiveTrue(String s);
    Page<Product> findAllByIsActiveTrue(Pageable pageable);
    Page<Product> findByIsActiveTrueAndCategory_IdOrderByPriceAsc(String categoryId, Pageable pageable);

    Page<Product> findByIsActiveTrueAndNameLikeIgnoreCase(String keyword, Pageable pageable);

    Page<Product> findByIsActiveTrueAndSeller_Id(String userId, Pageable pageable);

    @Query("""
       SELECT p.category.name
       FROM Product p
       WHERE p.isActive = true
       AND p.name = :productName AND p.category.id <> :categoryId
""")
    Optional<String> productBelongsToAnotherCategory(@Param("categoryId") String categoryId,
                                                     @Param("productName") String productName);


}
