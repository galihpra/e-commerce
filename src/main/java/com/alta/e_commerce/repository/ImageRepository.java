package com.alta.e_commerce.repository;

import com.alta.e_commerce.entity.Image;
import com.alta.e_commerce.entity.Product;
import com.alta.e_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    @Query("SELECT i FROM Image i WHERE i.product.id = :productId")
    List<Image> findByProductId(@Param("productId") String productId);
}


