package net.PORC.journalApp.service;

import net.PORC.journalApp.Repository.JournalEntryRepo;
import net.PORC.journalApp.dto.AIResponse;
import net.PORC.journalApp.entity.JournalEntry;
import net.PORC.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void SaveEntry(JournalEntry journalEntry, String username) {
        User user = userService.FindByUsername(username);

        // 🕒 set date
        journalEntry.setUsername(username);
        journalEntry.setDate(LocalDateTime.now());

        // 🤖 AI CALL
        AIResponse ai = geminiService.analyzeJournalAndParse(journalEntry.getContent());

        // 🧠 MAP AI → ENTITY
        if (ai != null) {
            journalEntry.setMood(ai.getMood());
            journalEntry.setAiSummary(ai.getSummary());
            journalEntry.setAiAdvice(ai.getAdvice());
        }

        // 💾 SAVE ENTRY
        JournalEntry saved = journalEntryRepo.save(journalEntry);

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

        List<JournalEntry> entries =
                journalEntryRepo.findTop10ByUsernameOrderByDateDesc(username);

        // ❌ No journal case
        if (entries.isEmpty()) {
            return "normal";
        }

        Map<String, Integer> moodCount = new HashMap<>();

        for (JournalEntry entry : entries) {
            String mood = entry.getMood();

            if (mood == null || mood.isEmpty()) continue;

            moodCount.put(mood, moodCount.getOrDefault(mood, 0) + 1);
        }

        // ❌ If somehow all moods null
        if (moodCount.isEmpty()) {
            return "normal";
        }

        // 🔥 Find most frequent mood
        return moodCount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

}