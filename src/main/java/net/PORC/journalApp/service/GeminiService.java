package net.PORC.journalApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.PORC.journalApp.dto.AIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
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

    public GeminiService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    // 🔹 CORE CALL METHOD
    public String callGemini(String prompt) {

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
                            .path("/v1beta/models/gemini-2.5-flash-lite:generateContent")
                            .queryParam("key", apiKey)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(status -> status.isError(), response ->
                            response.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        System.err.println("❌ GEMINI ERROR BODY: " + errorBody);
                                        return Mono.error(new RuntimeException("Gemini API Error"));
                                    })
                    )
                    .bodyToMono(String.class)
                    .doOnNext(res -> System.out.println("✅ RAW RESPONSE: " + res))
                    .block();

            // ✅ PARSE ONLY TEXT
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
            System.err.println("❌ GEMINI EXCEPTION: " + e.getMessage());
            return "ERROR_CALLING_GEMINI";
        }
    }

    // 🔹 JOURNAL ANALYSIS METHOD
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
    private String cleanResponse(String text) {
        if (text == null) return "";

        return text
                .replace("```json", "")
                .replace("```", "")
                .trim();
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
    public String chatWithMemory(String message, String context) {

        String prompt = """
    You are an emotionally intelligent AI companion inside a journaling app.

    Context about user:
    """ + context + """

    Current user message:
    """ + message + """

    Instructions:
    - Be human, not robotic. make sure to behave as humanly and warmly as possible
    - Keep it short (5-8 lines)
    - Be emotionally supportive
    - Use past context/conversation if relevant
    """;

        return callGemini(prompt);
    }
}
