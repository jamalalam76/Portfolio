package com.portfolio.controller;

import com.portfolio.dto.AuthDtos.ContactDto;
import com.portfolio.model.ContactMessage;
import com.portfolio.repository.ContactMessageRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactMessageRepository repo;
    public ContactController(ContactMessageRepository repo) { this.repo = repo; }

    @PostMapping
    public Map<String, Object> submit(@Valid @RequestBody ContactDto dto) {
        ContactMessage saved = repo.save(ContactMessage.builder()
                .name(dto.getName()).email(dto.getEmail()).message(dto.getMessage()).build());
        return Map.of("ok", true, "id", saved.getId());
    }
}
