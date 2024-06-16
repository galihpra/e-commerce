package com.alta.e_commerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @JsonIgnore
    private String id;

    @Size(max = 100)
    @Email
    private String email;


    @Size(max = 100)
    private String password;

    @Size(max = 100)
    private String name;

    @Size(max = 30)
    private String identifier;

    @Size(max = 20)
    private String phone;

    private String image;
    private MultipartFile file;

}

