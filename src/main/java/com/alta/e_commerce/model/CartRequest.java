package com.alta.e_commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequest {
    @NotBlank
    @JsonProperty("product_id")
    private String productId;

    @NotNull
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer qty;
}
