package net.PORC.journalApp.Controller;


import net.PORC.journalApp.dto.ChatRequest;
import net.PORC.journalApp.entity.ChatMessage;
import net.PORC.journalApp.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody ChatRequest request) {

        String response = chatService.getResponse(
                request.getMessage(),
                request.getUsername()
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/history/{username}")
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable String username) {

        List<ChatMessage> chats =
                chatService.getChatHistory(username);

        return ResponseEntity.ok(chats);
    }
}
