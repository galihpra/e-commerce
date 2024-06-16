package com.alta.e_commerce.service;

import com.alta.e_commerce.model.LoginRequest;
import com.alta.e_commerce.model.UserRequest;
import com.alta.e_commerce.entity.User;
import com.alta.e_commerce.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(UserRequest input) {
        User user = new User();

        user.setId(UUID.randomUUID().toString());
        user.setName(input.getName());
        user.setEmail(input.getEmail());
        user.setIdentifier(input.getIdentifier());
        user.setPhone(input.getPhone());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}

