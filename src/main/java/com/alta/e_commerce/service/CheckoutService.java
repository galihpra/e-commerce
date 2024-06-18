package com.alta.e_commerce.service;

import com.alta.e_commerce.entity.*;
import com.alta.e_commerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CheckoutService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    public void checkout(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Cart> carts = cartRepository.findByUser_Id(userId);

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transaction.setPaymentMethod("Online Payment");
        transaction.setStatus("Pending");
        transaction.setUser(user);

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<TransactionDetail> transactionDetails = new ArrayList<>();

        for (Cart cart : carts) {
            TransactionDetail transactionDetail = new TransactionDetail();
            transactionDetail.setId(UUID.randomUUID().toString());
            transactionDetail.setTransaction(transaction);
            transactionDetail.setProduct(cart.getProduct());
            transactionDetail.setQuantity(cart.getQty());

            BigDecimal price = BigDecimal.valueOf(cart.getProduct().getPrice());
            Integer quantity = cart.getQty();

            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));
            transactionDetail.setPrice(itemTotal);

            cart.getProduct().reduceStock(cart.getQty());
            productRepository.save(cart.getProduct());

            transactionDetails.add(transactionDetail);

            totalAmount = totalAmount.add(transactionDetail.getPrice());

            cartRepository.delete(cart);
        }

        transaction.setTotalAmount(totalAmount);

        transactionRepository.save(transaction);
        transactionDetailRepository.saveAll(transactionDetails);
    }
}