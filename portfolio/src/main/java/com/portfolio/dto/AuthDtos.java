package com.portfolio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class AuthDtos {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank @Size(min = 3, max = 50) private String username;
        @NotBlank @Email private String email;
        @NotBlank @Size(min = 6, max = 100) private String password;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank private String username;
        @NotBlank private String password;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private String username;
        private String role;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class ChatRequest {
        @NotBlank private String message;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class ChatResponse {
        private String reply;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class ContactDto {
        @NotBlank private String name;
        @NotBlank @Email private String email;
        @NotBlank @Size(min = 5, max = 2000) private String message;
    }
}
