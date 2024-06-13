package com.alta.e_commerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserResponse {
    private String id;

    private String name;

    private String email;

    private String username;

    private String phone;

    private String image;
}
