package com.alta.e_commerce.controller;

import com.alta.e_commerce.entity.Product;
import com.alta.e_commerce.entity.User;
import com.alta.e_commerce.model.ProductRequest;
import com.alta.e_commerce.model.ProductResponse;
import com.alta.e_commerce.model.UserResponse;
import com.alta.e_commerce.model.WebResponse;
import com.alta.e_commerce.service.JwtService;
import com.alta.e_commerce.service.ProductService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtService jwtService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<ProductResponse>> create(@RequestBody ProductRequest request, @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);  // Extract token from "Bearer " prefix

        String userId = jwtService.extractClaim(token, claims -> claims.get("user_id", String.class));

        ProductResponse productResponse = productService.create(request, userId);

        WebResponse<ProductResponse> response = WebResponse.<ProductResponse>builder()
                .message("success add data")
                .build();

        return ResponseEntity.ok(response);
    }


}
