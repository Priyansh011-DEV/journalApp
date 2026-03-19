package net.PORC.journalApp.service;

import net.PORC.journalApp.Repository.ChatMessageRepository;
import net.PORC.journalApp.Repository.JournalEntryRepo;
import net.PORC.journalApp.entity.ChatMessage;
import net.PORC.journalApp.entity.JournalEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatMessageRepository chatRepo;

    @Autowired
    private JournalEntryRepo journalRepository;

    @Autowired
    private GeminiService geminiService;

    public String getResponse(String message, String username) {

        // 🧠 1. Fetch last chats (memory)
        List<ChatMessage> chats =
                chatRepo.findTop5ByUsernameOrderByTimestampDesc(username);

        StringBuilder context = new StringBuilder();

        // 🧠 2. Base instruction (VERY IMPORTANT)
        context.append("""
        You are an emotionally intelligent AI companion inside a journaling app.

        Rules:
        - Be human, not robotic and be as warm as possible
        - Keep responses short (5-8 lines)
        - Be supportive and calm
        - If user is new, make them comfortable
        - Do NOT say "based on context"
        - Talk like you remember the user

        """);

        // 💬 3. Handle chat history (NEW USER FIX)
        if (chats.isEmpty()) {
            context.append("This is the first conversation with the user.\n");
        } else {
            context.append("Recent conversations:\n");

            for (ChatMessage c : chats) {
                context.append("User: ").append(c.getMessage()).append("\n");
                context.append("AI: ").append(c.getResponse()).append("\n");
            }
        }

        // 📖 4. Handle journal insights safely
        List<JournalEntry> entries = journalRepository.findByUsername(username);

        if (entries.isEmpty()) {
            context.append("\nNo journal entries yet.\n");
        } else {
            JournalEntry latest = entries.get(entries.size() - 1);

            context.append("\nUser's latest emotional state:\n");

            if (latest.getMood() != null && !latest.getMood().isEmpty()) {
                context.append("Mood: ").append(latest.getMood()).append("\n");
            }

            if (latest.getAiSummary() != null && !latest.getAiSummary().isEmpty()) {
                context.append("Summary: ").append(latest.getAiSummary()).append("\n");
            }

            if (latest.getAiAdvice() != null && !latest.getAiAdvice().isEmpty()) {
                context.append("Advice: ").append(latest.getAiAdvice()).append("\n");
            }
        }

        // 🧠 5. Add current user message
        context.append("\nUser: ").append(message).append("\nAI:");

        // 🤖 6. Call Gemini
        String aiResponse = geminiService.chatWithMemory(message, context.toString());

        // 💾 7. Save chat
        ChatMessage chat = new ChatMessage();
        chat.setUsername(username);
        chat.setMessage(message);
        chat.setResponse(aiResponse);
        chat.setTimestamp(LocalDateTime.now());

        chatRepo.save(chat);

        // 📤 8. Return response
        return aiResponse;
    }
    public List<ChatMessage> getChatHistory(String username) {
        return chatRepo.findTop5ByUsernameOrderByTimestampDesc(username);
    }

}
