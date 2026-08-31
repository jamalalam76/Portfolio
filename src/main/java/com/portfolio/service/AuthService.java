package com.portfolio.service;

import com.portfolio.dto.AuthDtos.*;
import com.portfolio.model.User;
import com.portfolio.repository.UserRepository;
import com.portfolio.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtUtil jwt) {
        this.users = users; this.encoder = encoder; this.jwt = jwt;
    }

    public AuthResponse register(RegisterRequest req) {
        if (users.existsByUsername(req.getUsername()))
            throw new IllegalArgumentException("Username already taken");
        if (users.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Email already registered");

        User u = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role("USER")
                .build();
        users.save(u);
        return new AuthResponse(jwt.generateToken(u.getUsername(), u.getRole()), u.getUsername(), u.getRole());
    }

    public AuthResponse login(LoginRequest req) {
        User u = users.findByUsername(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!encoder.matches(req.getPassword(), u.getPassword()))
            throw new IllegalArgumentException("Invalid credentials");
        return new AuthResponse(jwt.generateToken(u.getUsername(), u.getRole()), u.getUsername(), u.getRole());
    }
}
