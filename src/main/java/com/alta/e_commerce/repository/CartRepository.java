package com.alta.e_commerce.repository;

import com.alta.e_commerce.entity.Cart;
import com.alta.e_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    List<Cart> findByUserId(String userId);
    @Query("SELECT c FROM Cart c WHERE c.id = :cartId AND c.user.id = :userId")
    Optional<Cart> findByIdAndUserId(@Param("cartId") String cartId, @Param("userId") String userId);
}

