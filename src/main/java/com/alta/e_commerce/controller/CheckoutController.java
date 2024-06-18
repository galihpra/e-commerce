package com.alta.e_commerce.controller;

import com.alta.e_commerce.entity.Transaction;
import com.alta.e_commerce.model.*;
import com.alta.e_commerce.service.CheckoutService;
import com.alta.e_commerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<TransactionResponse>> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page)
                .limit(limit)
                .build();

        String token = authorizationHeader.substring(7);  // Extract token from "Bearer " prefix

        String userId = jwtService.extractClaim(token, claims -> claims.get("user_id", String.class));

        Page<TransactionResponse> transactionResponses = checkoutService.getAll(userId, request);

        return WebResponse.<List<TransactionResponse>>builder()
                .data(transactionResponses.getContent())
                .message("success get data")
                .pagination(PaginationResponse.builder()
                        .currentPage(transactionResponses.getNumber())
                        .totalPage(transactionResponses.getTotalPages())
                        .limit(transactionResponses.getSize())
                        .build())
                .build();
    }


}
