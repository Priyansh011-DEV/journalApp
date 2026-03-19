package net.PORC.journalApp.Controller;

import net.PORC.journalApp.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/AIres")
public class Geminicontroller {
    @Autowired
    GeminiService geminiService;

    @GetMapping("/test-ai")
    public String testAI() {
        try {
            return geminiService.callGemini("Say hello like a wise philosopher");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    @GetMapping("/test-journal")
    public String testJournal() {
        return geminiService.analyzeJournal(
                "I feel tired, unmotivated, and confused about life lately"
        );
    }
}
