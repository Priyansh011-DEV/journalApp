package net.PORC.journalApp.Repository;

import net.PORC.journalApp.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findTop5ByUsernameOrderByTimestampDesc(String username);
}
