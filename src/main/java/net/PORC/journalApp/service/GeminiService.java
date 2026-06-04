package net.PORC.journalApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.PORC.journalApp.dto.AIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient;

    // ── MODELS ──
    private static final String RAG_MODEL      = "gemini-3.1-flash-lite";   // 500 RPD — RAG + journal analysis
    private static final String CHAT_MODEL     = "gemma-4-31b-it";          // 1500 RPD — AI companion chat

    public GeminiService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    // ── CORE CALL — pass model explicitly ──
    private String callModel(String prompt, String model) {

        Map<String, Object> request = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        try {
            String rawResponse = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/" + model + ":generateContent")
                            .queryParam("key", apiKey)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(status -> status.isError(), response ->
                            response.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        System.err.println("❌ API ERROR [" + model + "]: " + errorBody);
                                        return Mono.error(new RuntimeException("API Error"));
                                    })
                    )
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawResponse);

            String text = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            return cleanResponse(text);

        } catch (Exception e) {
            System.err.println("❌ EXCEPTION [" + model + "]: " + e.getMessage());
            return "rate limit reached try after 24hr 🐰 refer to info page";
        }
    }

    // ── PUBLIC SHORTCUTS ──

    // RAG + journal analysis → gemini-3.1-flash-lite
    public String callGemini(String prompt) {
        return callModel(prompt, RAG_MODEL);
    }

    // Chat companion → gemma-4-31b-it
    public String callGemmaChat(String prompt) {
        return callModel(prompt, CHAT_MODEL);
    }

    // ── JOURNAL ANALYSIS (RAG model) ──
    public String analyzeJournal(String content) {
        String prompt = """
                Analyze this journal entry and return STRICT JSON only.
                Do NOT add extra text.

                Format:
                {
                  "mood": "",
                  "summary": "",
                  "advice": ""
                }

                Journal:
                """ + content;

        return callGemini(prompt);
    }

    public AIResponse analyzeJournalAndParse(String content) {
        String response = analyzeJournal(content);
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response, AIResponse.class);
        } catch (Exception e) {
            System.err.println("❌ JSON PARSE ERROR: " + e.getMessage());
            return null;
        }
    }

    // ── CHAT COMPANION (Gemma model) ──
    public String chatWithMemory(String message, String context) {
        String prompt = """
                You are an emotionally intelligent AI companion.

               
                

                
                

                Instructions:
                - Be human, not robotic. Be as warm as possible
                - Keep it short (5-8 lines)
                - Be emotionally supportive
                - Use past context/conversation if relevant
                """;

        return callGemmaChat(prompt);
    }

    // ── MOOD SUMMARY (RAG model) ──
    public String getMoodSummary(String mood, Map<String, Integer> scores) {
        String prompt = "User mood analysis:\n" +
                "Dominant Mood: " + mood + "\n" +
                "Scores: " + scores + "\n\n" +
                "Give short emotional insight and advice in 2-3 lines.";

        return callGemini(prompt);
    }

    private String cleanResponse(String text) {
        if (text == null) return "";
        return text
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}