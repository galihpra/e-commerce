package com.alta.e_commerce.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartUpdateRequest {
    @NotNull
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer qty;
}
