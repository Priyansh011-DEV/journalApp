package net.PORC.journalApp.dto;

import java.util.Map;

public class AnalysisResult {


        private String mood;
        private Map<String, Integer> scores;
        private String advice;

        public AnalysisResult(String mood, Map<String, Integer> scores, String advice) {
            this.mood = mood;
            this.scores = scores;
            this.advice = advice;
        }

        public String getMood() {
            return mood;
        }

        public Map<String, Integer> getScores() {
            return scores;
        }

        public String getAdvice() {
            return advice;
        }
    }
