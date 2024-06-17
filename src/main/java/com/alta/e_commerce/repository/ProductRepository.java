package com.alta.e_commerce.repository;

import com.alta.e_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByUserId(String userId);

    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.user.id = :userId")
    Optional<Product> findByIdAndUserId(@Param("productId") String productId, @Param("userId") String userId);
}

