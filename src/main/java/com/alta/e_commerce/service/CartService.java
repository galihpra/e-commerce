package com.alta.e_commerce.service;

import com.alta.e_commerce.entity.Cart;
import com.alta.e_commerce.entity.Image;
import com.alta.e_commerce.entity.Product;
import com.alta.e_commerce.entity.User;
import com.alta.e_commerce.model.*;
import com.alta.e_commerce.repository.CartRepository;
import com.alta.e_commerce.repository.ProductRepository;
import com.alta.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ValidationService validationService;

    private String getUserIdFromToken(String token) {
        return jwtService.extractClaim(token, claims -> token);
    }

    @Transactional
    public void create(CartRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        Cart cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setProduct(product);
        cart.setQty(request.getQty());
        cart.setUser(user);

        cartRepository.save(cart);
    }

    @Transactional
    public void delete(String cartId, String userId) {
        Cart cart = cartRepository.findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "cart tidak ditemukan"));

        cartRepository.delete(cart);
    }

    @Transactional
    public void update(Integer qty, String productId, String userId){
        Cart cart = cartRepository.findByIdAndUserId(productId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "keranjang tidak ditemukan"));

        if (qty != null) {
            cart.setQty(qty);
        }

        cartRepository.save(cart);
    }

}
