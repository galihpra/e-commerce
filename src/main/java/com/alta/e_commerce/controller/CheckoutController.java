package com.alta.e_commerce.controller;

import com.alta.e_commerce.entity.Transaction;
import com.alta.e_commerce.model.WebResponse;
import com.alta.e_commerce.service.CheckoutService;
import com.alta.e_commerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping()
    public WebResponse<String> checkout(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);  // Extract token from "Bearer " prefix

        String userId = jwtService.extractClaim(token, claims -> claims.get("user_id", String.class));

        checkoutService.checkout(userId);

        return WebResponse.<String>builder()
                .message("checkout success")
                .build();
    }

}
