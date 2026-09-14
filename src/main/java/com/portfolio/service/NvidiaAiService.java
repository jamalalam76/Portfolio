package com.portfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class NvidiaAiService {

    @Value("${nvidia.api.key}")
    private String apiKey;

    @Value("${nvidia.api.url}")
    private String apiUrl;

    @Value("${nvidia.api.model}")
    private String configuredModel;

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

        List<String> candidateModels = List.of(
                configuredModel != null && !configuredModel.isBlank() ? configuredModel : "meta/llama-3.3-70b-instruct",
                "meta/llama-3.3-70b-instruct",
                "meta/llama-3.1-8b-instruct",
                "deepseek-ai/deepseek-r1"
        );

        for (String targetModel : candidateModels) {
            try {
                Map<String, Object> body = Map.of(
                        "model", targetModel,
                        "messages", List.of(
                                Map.of("role", "system", "content", systemPrompt),
                                Map.of("role", "user", "content", userMessage)
                        ),
                        "temperature", 0.6,
                        "top_p", 0.9,
                        "max_tokens", 700,
                        "stream", false
                );

                Map<?, ?> resp = client.post()
                        .uri(apiUrl)
                        .header("Authorization", "Bearer " + apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();

                if (resp != null) {
                    List<?> choices = (List<?>) resp.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<?, ?> first = (Map<?, ?>) choices.get(0);
                        Map<?, ?> msg = (Map<?, ?>) first.get("message");
                        if (msg != null && msg.get("content") != null) {
                            return String.valueOf(msg.get("content"));
                        }
                    }
                }
            } catch (WebClientResponseException e) {
                if (e.getStatusCode().value() == 410 || e.getStatusCode().value() == 404) {
                    continue;
                }
                return "AI response error (" + e.getStatusCode().value() + "): " + e.getResponseBodyAsString();
            } catch (Exception e) {
                return "AI error: " + e.getMessage();
            }
        }

        return "Hi! I'm " + ownerName + "'s AI Assistant. You asked: \"" + userMessage + "\". "
             + "MD Jamal is a B.Tech IT student building web applications with Java, Spring Boot, React.js, and clean UI!";
    }
}
