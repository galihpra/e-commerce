package com.alta.e_commerce.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCartRequest {

    private String name;

    private Integer qty;

    private double price;

    @NotNull
    private Integer page;

    @NotNull
    private Integer limit;
}

