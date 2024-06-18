package com.alta.e_commerce.repository;

import com.alta.e_commerce.entity.Product;
import com.alta.e_commerce.entity.User;
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
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.user.id = :userId")
    Optional<Product> findByIdAndUserId(@Param("productId") String productId, @Param("userId") String userId);
}

