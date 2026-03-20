package net.PORC.journalApp.service;


import net.PORC.journalApp.entity.JournalEntry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MoodAggregationService {
    // 🔢 Combine all scores
    public Map<String, Integer> aggregateScores(List<JournalEntry> journals) {
        Map<String, Integer> total = new HashMap<>();

        for (JournalEntry j : journals) {
            if (j.getMoodScore() == null) continue;

            for (var entry : j.getMoodScore().entrySet()) {
                total.put(entry.getKey(),
                        total.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        return total;
    }

    // 🧠 Get dominant mood
    public String getDominantMood(Map<String, Integer> scores) {

        if (scores.isEmpty()) return "neutral";

        boolean allZero = scores.values().stream().allMatch(v -> v == 0);
        if (allZero) return "neutral";

        return scores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }
}
