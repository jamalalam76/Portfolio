package com.portfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class NvidiaAiService {

    @Value("${nvidia.api.key}")
    private String apiKey;

    @Value("${nvidia.api.url}")
    private String apiUrl;

    @Value("${nvidia.api.model}")
    private String model;

    @Value("${portfolio.owner.name}")
    private String ownerName;

    @Value("${portfolio.owner.title}")
    private String ownerTitle;

    @Value("${portfolio.owner.email}")
    private String ownerEmail;

    private final WebClient client = WebClient.builder()
            .codecs(c -> c.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
            .build();

    public String ask(String userMessage) {
        if (apiKey == null || apiKey.isBlank() || apiKey.contains("REPLACE_WITH_YOUR_KEY")) {
            return "AI is not configured yet. Set NVIDIA_API_KEY in application.properties to enable AI replies. "
                 + "Meanwhile: I'm " + ownerName + ", a " + ownerTitle + ". You asked: " + userMessage;
        }

        String systemPrompt = """
            You are an AI assistant embedded in %s's personal portfolio website.
            Owner: %s — %s (contact: %s).
            Help visitors learn about the owner, analyze the portfolio, suggest improvements,
            and answer general technical questions. Be concise, friendly and constructive.
            """.formatted(ownerName, ownerName, ownerTitle, ownerEmail);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                ),
                "temperature", 0.6,
                "top_p", 0.9,
                "max_tokens", 700,
                "stream", false
        );

        try {
            Map<?, ?> resp = client.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (resp == null) return "No response from AI.";
            List<?> choices = (List<?>) resp.get("choices");
            if (choices == null || choices.isEmpty()) return "AI returned no choices.";
            Map<?, ?> first = (Map<?, ?>) choices.get(0);
            Map<?, ?> msg = (Map<?, ?>) first.get("message");
            return msg == null ? "Empty AI message." : String.valueOf(msg.get("content"));
        } catch (Exception e) {
            return "AI error: " + e.getMessage();
        }
    }
}
