package com.portfolio.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ContactMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Column(length = 2000)
    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();
}
