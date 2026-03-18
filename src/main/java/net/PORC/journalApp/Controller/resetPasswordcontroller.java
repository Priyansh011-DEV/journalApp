package net.PORC.journalApp.Controller;


import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.PasswordResetToken;
import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.PasswordResetService;
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
    private PasswordResetService passwordResetService;


    @PostMapping("/Forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

// 🔒 Do NOT reveal if user exists
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok("Gmail do not exist");
        }

        User existingUser = userOptional.get();

// 🔥 EMAIL VALIDATION
        if (user.getEmail() == null ||
                !existingUser.getEmail().equals(user.getEmail())) {

            return ResponseEntity.ok("If account exists with gmail , token generated");
        }

// 🔥 create + save token
        PasswordResetToken token = passwordResetService.createAndSaveToken(existingUser.getEmail());

// 🔥 return token (for your no-email system)
        return ResponseEntity.ok(token.getToken());
    }


    @PutMapping("/Reset")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestBody User user) {

        // 🔒 validate + get token
        PasswordResetToken prt = passwordResetService.validateToken(token);

// 🔍 get user from token email
        Optional<User> userOptional = userRepository.findByEmail(prt.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User existingUser = userOptional.get();

// 🔐 update password
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(existingUser);

// 🔥 mark token used (reuse object instead of re-fetch)
        prt.setUsed(true);
        passwordResetService.save(prt); // we'll add this method

        return ResponseEntity.ok("Password reset successful 🔥");
    }
}