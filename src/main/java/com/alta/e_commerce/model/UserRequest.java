package com.alta.e_commerce.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    @Size(max = 30)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;

    @Size(max = 100)
    private String name;

    @Size(max = 15)
    private String phone;
}
