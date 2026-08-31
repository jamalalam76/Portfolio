package com.portfolio.controller;

import com.portfolio.dto.AuthDtos.*;
import com.portfolio.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        try { return ResponseEntity.ok(auth.register(req)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try { return ResponseEntity.ok(auth.login(req)); }
        catch (Exception e) { return ResponseEntity.status(401).body(Map.of("error", e.getMessage())); }
    }
}
