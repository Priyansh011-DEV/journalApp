package net.PORC.journalApp.Repository;

import com.mongodb.client.MongoDatabase;
import net.PORC.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JournalEntryRepo extends MongoRepository<JournalEntry, String> {

    List<JournalEntry> id(ObjectId id);

    void deleteById(String id);
    List<JournalEntry> findByUsername(String username);
    List<JournalEntry> findTop10ByUsernameOrderByDateDesc(String username);

    List<JournalEntry> findByUsernameAndDateAfter(String username, LocalDateTime date);
}
