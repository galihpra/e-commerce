package com.alta.e_commerce.repository;

import com.alta.e_commerce.entity.Cart;
import com.alta.e_commerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String>, JpaSpecificationExecutor<Cart> {
    Page<Cart> findByUserId(String userId, Pageable pageable);

    @Query("SELECT c FROM Cart c WHERE c.id = :cartId AND c.user.id = :userId")
    Optional<Cart> findByIdAndUserId(@Param("cartId") String cartId, @Param("userId") String userId);
}

