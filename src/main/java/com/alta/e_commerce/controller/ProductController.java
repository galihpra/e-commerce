package com.alta.e_commerce.controller;

import com.alta.e_commerce.entity.Product;
import com.alta.e_commerce.entity.User;
import com.alta.e_commerce.model.*;
import com.alta.e_commerce.service.JwtService;
import com.alta.e_commerce.service.ProductService;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtService jwtService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public WebResponse<ProductResponse> create(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("stock") int stock,
            @RequestParam("images") List<MultipartFile> images,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);  // Extract token from "Bearer " prefix

        String userId = jwtService.extractClaim(token, claims -> claims.get("user_id", String.class));

        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        request.setStock(stock);
        request.setImageUrls(images);

        ProductResponse productResponse = productService.create(request, userId);
        return WebResponse.<ProductResponse>builder()
                .message("Product created successfully")
                .data(productResponse)
                .build();
    }

    @DeleteMapping(
            path = "/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(
            @PathVariable("productId") String productId,
            @RequestHeader("Authorization") String authorizationHeader
    ){
        String token = authorizationHeader.substring(7);  // Extract token from "Bearer " prefix

        String userId = jwtService.extractClaim(token, claims -> claims.get("user_id", String.class));

        productService.delete(productId, userId);
        return WebResponse.<String>builder()
                .message("success delete data")
                .build();
    }

    @PutMapping(
            path = "/{productId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public WebResponse<ProductResponse> update(
            @PathVariable("productId") String productId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);  // Extract token from "Bearer " prefix

        String userId = jwtService.extractClaim(token, claims -> claims.get("user_id", String.class));

        UpdateProductRequest request = UpdateProductRequest.builder()
                .id(productId)
                .userId(userId)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .imageUrls(images)
                .build();

        ProductResponse productResponse = productService.update(request, productId, userId);
        return WebResponse.<ProductResponse>builder()
                .message("Product updated successfully")
                .data(productResponse)
                .build();
    }


    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ProductResponse>> getAllorSearch(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
    ){
        SearchProductRequest request = SearchProductRequest.builder()
                .page(page)
                .limit(limit)
                .name(name)
                .build();

        Page<ProductResponse> productResponses = productService.getAllOrSearch(request);
        return WebResponse.<List<ProductResponse>>builder()
                .data(productResponses.getContent())
                .message("success get data")
                .pagination(PaginationResponse.builder()
                        .currentPage(productResponses.getNumber())
                        .totalPage(productResponses.getTotalPages())
                        .limit(productResponses.getSize())
                        .build())
                .build();
    }

    @GetMapping(
            path = "/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> getById(
            @PathVariable("productId") String productId
    ){
        ProductResponse productResponse = productService.getById(productId);
        return WebResponse.<ProductResponse>builder()
                .message("success get data")
                .data(productResponse)
                .build();
    }
}
