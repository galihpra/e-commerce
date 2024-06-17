package com.alta.e_commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    private String id;

    @Size(max = 100)
    private String name;

    private String description;

    private double price;

    private Integer stock;

    @Size(max = 255)
    @JsonProperty("user_id")
    private String userId;

    private List<MultipartFile> imageUrls;
}
