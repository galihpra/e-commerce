package com.alta.e_commerce.controller;

import com.alta.e_commerce.model.*;
import com.alta.e_commerce.service.CartService;
import com.alta.e_commerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtService jwtService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CartResponse> create(
            @RequestBody CartRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);  // Extract token from "Bearer " prefix

        String userId = jwtService.extractClaim(token, claims -> claims.get("user_id", String.class));

        cartService.create(request, userId);

        return WebResponse.<CartResponse>builder()
                .message("Cart created successfully")
                .build();
    }

    @DeleteMapping(
            path = "/{cartId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(
            @PathVariable("cartId") String cartId,
            @RequestHeader("Authorization") String authorizationHeader
    ){
        String token = authorizationHeader.substring(7);  // Extract token from "Bearer " prefix

        String userId = jwtService.extractClaim(token, claims -> claims.get("user_id", String.class));

        cartService.delete(cartId, userId);
        return WebResponse.<String>builder()
                .message("success delete data")
                .build();
    }

    @PutMapping(
            path = "/{cartId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> update(
            @PathVariable("cartId") String cartId,
            @RequestBody CartUpdateRequest cartUpdateRequest,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);  // Extract token from "Bearer " prefix

        String userId = jwtService.extractClaim(token, claims -> claims.get("user_id", String.class));

        logger.info("Received request to update cart with qty: {}, productId: {}, userId: {}", cartUpdateRequest.getQty(), cartId, userId);
        cartService.update(cartUpdateRequest.getQty(), cartId, userId);
        return WebResponse.<String>builder()
                .message("Cart updated successfully")
                .build();
    }
}
