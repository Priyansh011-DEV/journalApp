package net.PORC.journalApp.Controller;


import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/ResetPassword")
public class resetPasswordcontroller {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;



    @PostMapping("/Forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody User user) {

        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

        // 🔐 SECURITY: don't reveal if user exists
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok("If account exists, token sent to email 📧");
        }

        User existingUser = userOptional.get();

        String token = UUID.randomUUID().toString();

        existingUser.setResetToken(token);
        existingUser.setTokenExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(existingUser);

        // 📧 SEND EMAIL
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(existingUser.getEmail()); // 🔐 from DB only
        message.setSubject("Password Reset Request 🔐");

        message.setText(
                "Hello " + existingUser.getUsername() + ",\n\n" +
                        "Your password reset token is:\n\n" +
                        token +
                        "\n\nThis token is valid for 10 minutes.\n\n" +
                        "If you did not request this, ignore this email."
        );

        mailSender.send(message);

        return ResponseEntity.ok("If account exists, token sent to email 📧");
    }


    @PutMapping("/Reset")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestBody User user) {

        User existingUser = userRepository.findByResetToken(token);

        if (existingUser == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        if (existingUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expired");
        }

        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));


        existingUser.setResetToken(null);
        existingUser.setTokenExpiry(null);

        userRepository.save(existingUser);

        return ResponseEntity.ok("Password reset successful 🔥");
    }
}
