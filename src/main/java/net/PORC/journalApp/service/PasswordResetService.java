package net.PORC.journalApp.service;

import net.PORC.journalApp.Repository.PasswordResetRepo;
import net.PORC.journalApp.entity.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetRepo passwordResetRepo;

    public PasswordResetToken createAndSaveToken(String email) {
        PasswordResetToken token = PasswordResetToken.builder()
                .email(email)
                .token(UUID.randomUUID().toString())
                .expiryTime(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        return passwordResetRepo.save(token);
    }
    public PasswordResetToken save(PasswordResetToken token) {
        return passwordResetRepo.save(token);
    }
    public PasswordResetToken validateToken(String token) {
        PasswordResetToken prt = passwordResetRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (prt.isUsed()) {
            throw new RuntimeException("Token already used");
        }

        if (prt.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        return prt;
    }
}
