package net.PORC.journalApp.Repository;

import net.PORC.journalApp.entity.JournalEntry;
import net.PORC.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {

Optional<User> findByUsername(String username);
}
