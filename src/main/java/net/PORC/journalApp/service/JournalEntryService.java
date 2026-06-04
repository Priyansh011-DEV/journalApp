package net.PORC.journalApp.service;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import net.PORC.journalApp.Repository.JournalEntryRepo;
import net.PORC.journalApp.dto.AnalysisResult;
import net.PORC.journalApp.entity.JournalEntry;
import net.PORC.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepo journalEntryRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private GeminiService geminiService;
    @Autowired
    private LocalAIService localAIService;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;

    @Transactional
    public void SaveEntry(JournalEntry journalEntry, String username) {
        User user = userService.FindByUsername(username);

        // 🕒 set date
        journalEntry.setUsername(username);
        journalEntry.setDate(LocalDateTime.now());

        // 🤖 AI CALL
        AnalysisResult result = localAIService.analyze(journalEntry.getContent());

        journalEntry.setMood(result.getMood());
        journalEntry.setMoodScore(result.getScores());
        journalEntry.setAiAdvice(result.getAdvice());
        journalEntry.setAiSource("LOCAL");

        // DEPRECIATED LOGIC 🧠 MAP AI → ENTITY//


        // 💾 SAVE ENTRY
        JournalEntry saved = journalEntryRepo.save(journalEntry);
        embedAndStore(saved);

        // 🔗 LINK TO USER
        user.getJournalEntries().add(saved);

        // 💾 SAVE USER
        userService.SaveUser(user);

    }


    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();

    }


    public Optional<JournalEntry> Find_byID(String id) {
        return journalEntryRepo.findById(id);

    }


    public boolean DeleteEntryByID(String id, String username) {
        User user = userService.FindByUsername(username);
        if( user == null){
            return false;

        }
        boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        if(!removed){
            return false;
        }
        userService.SaveUser(user);
        journalEntryRepo.deleteById(id);
        return true;
    }

    public Optional<JournalEntry> FindByUserID(String username, String id) {
        User user = userService.FindByUsername(username);
        if (user == null) {
            return Optional.empty();
        }

        return user.getJournalEntries().stream().filter(entry -> entry.getId().equals(id)).findFirst();
    }
    public String getTodayMood(String username) {

        LocalDateTime last24h = LocalDateTime.now().minusHours(24);

        List<JournalEntry> entries =
                journalEntryRepo.findByUsernameAndDateAfter(username, last24h);

        Map<String, Integer> totalScores = new HashMap<>();

        for (JournalEntry entry : entries) {
            if (entry.getMoodScore() == null) continue;

            for (var e : entry.getMoodScore().entrySet()) {
                totalScores.put(e.getKey(),
                        totalScores.getOrDefault(e.getKey(), 0) + e.getValue());
            }
        }

        if (totalScores.isEmpty()) return "neutral";

        return totalScores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    private void embedAndStore(JournalEntry entry) {
        try {
            String text = "Date: " + entry.getDate() +
                    "\nMood: " + entry.getMood() +
                    "\nEntry: " + entry.getContent();

            TextSegment segment = TextSegment.from(text,
                    Metadata.from("username", entry.getUsername()));

            Embedding embedding = embeddingModel.embed(segment).content();
            embeddingStore.add(embedding, segment);

            System.out.println("✅ Journal entry embedded for: " + entry.getUsername());
        } catch (Exception e) {
            System.err.println("❌ Embedding failed: " + e.getMessage());
        }
    }


    public String askJournal(String question, String username) {
        try {
            System.out.println("🔵 RAG ASK - question: " + question + " user: " + username);

            Embedding queryEmbedding = embeddingModel.embed(question).content();
            System.out.println("🟢 Embedding done");

            EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                    .queryEmbedding(queryEmbedding)
                    .maxResults(3)
                    .filter(MetadataFilterBuilder.metadataKey("username").isEqualTo(username))
                    .build();

            var matches = embeddingStore.search(request).matches();
            System.out.println("🟢 Matches found: " + matches.size());

            if (matches.isEmpty()) {
                return "No relevant journal entries found.";
            }

            StringBuilder context = new StringBuilder();
            context.append("Here are relevant journal entries:\n\n");
            for (var match : matches) {
                context.append(match.embedded().text()).append("\n\n");
            }

            String prompt = context + "\nBased on these journal entries, answer: " + question;
            System.out.println("🟢 Calling Gemini...");
            return geminiService.callGemini(prompt);

        } catch (Exception e) {
            System.err.println("❌ RAG ASK ERROR: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}