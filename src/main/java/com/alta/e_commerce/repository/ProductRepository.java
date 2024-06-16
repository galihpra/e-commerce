package com.alta.e_commerce.repository;

import com.alta.e_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserId(String userId);
}

