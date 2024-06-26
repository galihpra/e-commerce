package com.alta.e_commerce.controller;


import com.alta.e_commerce.service.UserService;
import com.alta.e_commerce.model.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;

@RestController
public class UserController {
    //inject
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PutMapping(
            path = "/users/{userId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "name", required = false) @Size(max = 100) String name,
            @RequestParam(value = "email", required = false) @Size(max = 100) @Email String email,
            @RequestParam(value = "password", required = false) @Size(max = 100) String password,
            @RequestParam(value = "identifier", required = false) @Size(max = 30) String identifier,
            @RequestParam(value = "phone", required = false) @Size(max = 20) String phone,
            @PathVariable("userId") String userId
    ) {
        // Buat objek request dari parameter yang diterima
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(userId)
                .name(name)
                .email(email)
                .password(password)
                .identifier(identifier)
                .phone(phone)
                .file(file)
                .build();


        UserResponse userResponse = userService.update(request);
        return WebResponse.<UserResponse>builder()
                .message("success update data")
                .data(userResponse)
                .build();
    }

    @DeleteMapping(
            path = "/users/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(
            @PathVariable("userId") String userId
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userService.delete(userId);
        return WebResponse.<String>builder()
                .message("success delete data")
                .build();
    }

    @GetMapping(
            path = "/users/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> getById(
            @PathVariable("userId") String userId
    ){
        UserResponse userResponse = userService.getById(userId);
        return WebResponse.<UserResponse>builder()
                .message("success get data")
                .data(userResponse)
                .build();
    }

    @GetMapping(
            path = "/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<UserResponse>> getAllOrSearch(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
    ){
        SearchUserRequest request = SearchUserRequest.builder()
                .name(name)
                .email(email)
                .page(page)
                .limit(limit)
                .build();

        Page<UserResponse> userResponses = userService.getAllOrSearch(request);
        return WebResponse.<List<UserResponse>>builder()
                .message("success get data")
                .data(userResponses.getContent())
                .pagination(PaginationResponse.builder()
                        .currentPage(userResponses.getNumber())
                        .totalPage(userResponses.getTotalPages())
                        .limit(userResponses.getSize())
                        .build())
                .build();
    }

}