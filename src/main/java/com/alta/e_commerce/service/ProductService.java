package com.alta.e_commerce.service;

import com.alta.e_commerce.entity.Product;
import com.alta.e_commerce.entity.User;
import com.alta.e_commerce.model.ProductRequest;
import com.alta.e_commerce.model.ProductResponse;
import com.alta.e_commerce.repository.ProductRepository;
import com.alta.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ValidationService validationService;

    private String getUserIdFromToken(String token) {
        return jwtService.extractClaim(token, claims -> token);
    }

    /**
     * method untuk mapping dari entity ke useresponse
     * @param product
     * @return
     */
    private ProductResponse toProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    @Transactional
    public ProductResponse create(ProductRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setUser(user);

        Product savedProduct = productRepository.save(product);

        return toProductResponse(savedProduct);
    }

}
