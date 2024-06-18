package com.alta.e_commerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private String id;

    private String productId;

    private String name;

    private Double price;

    private Integer qty;

}
