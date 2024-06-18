package com.alta.e_commerce.controller;

import com.alta.e_commerce.model.*;
import com.alta.e_commerce.service.CartService;
import com.alta.e_commerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/carts")
public class CartController {

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

        cartService.update(cartUpdateRequest.getQty(), cartId, userId);
        return WebResponse.<String>builder()
                .message("Cart updated successfully")
                .build();
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<CartResponse>> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
            @RequestHeader("Authorization") String authorizationHeader
    ){

        SearchCartRequest request = SearchCartRequest.builder()
                .page(page)
                .limit(limit)
                .build();

        String token = authorizationHeader.substring(7);  // Extract token from "Bearer " prefix

        String userId = jwtService.extractClaim(token, claims -> claims.get("user_id", String.class));

        Page<CartResponse> cartResponses = cartService.getAll(userId, request);

        return WebResponse.<List<CartResponse>>builder()
                .data(cartResponses.getContent())
                .message("success get data")
                .pagination(PaginationResponse.builder()
                        .currentPage(cartResponses.getNumber())
                        .totalPage(cartResponses.getTotalPages())
                        .limit(cartResponses.getSize())
                        .build())
                .build();
    }
}
