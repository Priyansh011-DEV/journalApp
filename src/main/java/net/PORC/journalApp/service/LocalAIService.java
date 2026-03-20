package net.PORC.journalApp.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import net.PORC.journalApp.dto.AnalysisResult;
import net.PORC.journalApp.entity.MoodContainer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class LocalAIService {

    private Map<String, MoodContainer> moodData;
    private final Random random = new Random();

    @PostConstruct
    public void loadMoodData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream = getClass()
                .getResourceAsStream("/mood-data.json");

        moodData = mapper.readValue(inputStream,
                new TypeReference<Map<String, MoodContainer>>() {});
    }

    // 🔍 MAIN ANALYSIS METHOD
    public AnalysisResult analyze(String text) {
        String normalized = normalize(text);
        List<String> words = List.of(normalized.split("\\s+"));

        Map<String, Integer> scores = new HashMap<>();

        for (var entry : moodData.entrySet()) {
            String mood = entry.getKey();
            MoodContainer container = entry.getValue();

            int score = 0;

            for (String keyword : container.getKeywords()) {
                for (String word : words) {
                    if (word.equals(keyword.toLowerCase())) {
                        score += getWeight(keyword);
                    }
                }
            }

            scores.put(mood, score);
        }

        String dominantMood = detectMood(scores);
        String advice = getAdvice(dominantMood);

        return new AnalysisResult(dominantMood, scores, advice);
    }

    // 🧠 Detect dominant mood
    private String detectMood(Map<String, Integer> scores) {

        boolean allZero = scores.values().stream().allMatch(v -> v == 0);
        if (allZero) return "neutral";

        return scores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    // 🎲 Advice
    private String getAdvice(String mood) {
        MoodContainer container = moodData.get(mood);

        if (container == null || container.getAdvice().isEmpty()) {
            return "Stay balanced and take care.";
        }

        List<String> adviceList = container.getAdvice();
        return adviceList.get(random.nextInt(adviceList.size()));
    }

    // ⚖️ Weight logic (simple but powerful)
    private int getWeight(String keyword) {
        if (keyword.length() > 8) return 3; // strong words like "depressed"
        if (keyword.length() > 5) return 2;
        return 1;
    }

    // 🧼 Clean text
    private String normalize(String text) {
        return text.toLowerCase().replaceAll("[^a-zA-Z ]", "");
    }
}
