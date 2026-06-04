package net.PORC.journalApp.Controller;

import net.PORC.journalApp.service.JournalEntryService;
import net.PORC.journalApp.service.RagTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class HealthCHK {

    @Autowired
    private RagTestService ragTestService;

    @Autowired
    private JournalEntryService journalEntryService;

    // ── PUBLIC ──
    @GetMapping("/health_check")
    public String healthCHK() {
        return "system ok";
    }

    // ── RAG TEST (dev only, remove before production) ──
    @GetMapping("/apiv2/rag/test")
    public String testRag() {
        return ragTestService.testEmbedding();
    }

    @GetMapping("/apiv2/rag/search")
    public String testSearch() {
        return ragTestService.testSearch();
    }

    // ── RAG Q&A ──
    @PostMapping("/apiv2/ask")
    public ResponseEntity<String> askJournal(
            @RequestBody String question,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        try {
            String username = principal.getName();
            System.out.println("🔵 RAG /ask - user: " + username);
            String answer = journalEntryService.askJournal(question, username);
            return ResponseEntity.ok(answer);
        } catch (Exception e) {
            System.err.println("❌ RAG ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}