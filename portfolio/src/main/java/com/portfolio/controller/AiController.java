package com.portfolio.controller;

import com.portfolio.dto.AuthDtos.*;
import com.portfolio.service.NvidiaAiService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final NvidiaAiService ai;
    public AiController(NvidiaAiService ai) { this.ai = ai; }

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest req) {
        return new ChatResponse(ai.ask(req.getMessage()));
    }
}
