package com.alta.e_commerce.repository;

import com.alta.e_commerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    List<Cart> findByUserId(String userId);
}

