package com.alta.e_commerce.repository;

import com.alta.e_commerce.entity.TransactionDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {
    @Query("SELECT td FROM TransactionDetail td JOIN td.transaction t WHERE t.user.id = :userId")
    Page<TransactionDetail> findByUserId(@Param("userId") String userId, Pageable pageable);
}
