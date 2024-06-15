package com.alta.e_commerce.controller;

import com.alta.e_commerce.helper.JwtHelper;
import com.alta.e_commerce.service.UserService;
import com.alta.e_commerce.model.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    //inject
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(
            path = "/signup",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> add(@RequestBody UserRequest request){
        userService.signup(request);
        return WebResponse.<String>builder()
                .message("success add data")
                .build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            String userId = userService.getUserIdByEmail(request.getEmail());
            String token = JwtHelper.generateToken(userId);
            return ResponseEntity.ok(new JwtAuthenticationResponse(request.getEmail(), token));
        } catch (UsernameNotFoundException e) {
            // Handle invalid username case (e.g., return 401 with a message)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtAuthenticationResponse(null,"Invalid username or password"));
        } catch (AuthenticationException e) {
            // Handle other authentication failures (e.g., return 401 with a generic message)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtAuthenticationResponse(null, "Authentication failed"));
        }
    }


    @PutMapping(
            path = "/users/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(
            @RequestBody UpdateUserRequest request,
            @PathVariable("userId") String userId
    ){
        // ambil dari url param dan set ke var request
        request.setId(userId);

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