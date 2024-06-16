package com.alta.e_commerce.controller;

import com.alta.e_commerce.entity.User;
import com.alta.e_commerce.model.LoginRequest;
import com.alta.e_commerce.model.UserRequest;
import com.alta.e_commerce.model.LoginResponse;
import com.alta.e_commerce.service.AuthenticationService;
import com.alta.e_commerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/signup",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody UserRequest request) {
        User registeredUser = authenticationService.signup(request);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest request) {
        User authenticatedUser = authenticationService.authenticate(request);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

//    public class UserController {
//
//        //inject
//        @Autowired
//        private UserService userService;
//
//        @Autowired
//        private AuthenticationManager authenticationManager;
//
//        @PostMapping(
//                path = "/signup",
//                consumes = MediaType.APPLICATION_JSON_VALUE,
//                produces = MediaType.APPLICATION_JSON_VALUE
//        )
//        public WebResponse<String> add(@RequestBody UserRequest request){
//            userService.signup(request);
//            return WebResponse.<String>builder()
//                    .message("success add data")
//                    .build();
//        }
}
