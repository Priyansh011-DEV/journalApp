package net.PORC.journalApp.Repository;

import net.PORC.journalApp.entity.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetRepo extends MongoRepository<PasswordResetToken , String> {
    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByEmail(String email);
}

