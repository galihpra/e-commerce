package com.alta.e_commerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductResponse {
    private String id;

    private String name;

    private String description;

    private double price;

    private Integer stock;

    private String thumbnail;

    private List<String> imageUrls;

    private String userId;
}
