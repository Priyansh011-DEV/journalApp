package net.PORC.journalApp.service;

import net.PORC.journalApp.Exceptionhandler.UserNotFoundException;
import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;
import java.util.Arrays;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired(required = false)
    private JavaMailSender mailSender;


    public User SaveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> Find_byID(ObjectId id) {
        return userRepository.findById(id);
    }

    public void DeleteEntryByID(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User FindByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("user not found  " + username));
    }

    public void UpdateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void RegisterUser(User user) {
        // 🔍 NULL CHECKS
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        // 🔐 CHECK EMAIL UNIQUE (fixed — Optional now)
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // 🔐 ENCODE PASSWORD
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));

        // 💾 SAVE USER
        userRepository.save(user);


    }

    public User createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("ADMIN"));
        return userRepository.save(user);
    }

    public void makeuser(User user) {
        userRepository.save(user);
    }

    @Async
    public void sendResetEmail(String toEmail, String username, String token) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Password Reset 🔐");
            message.setText("Hello " + username + ",\n\nYour token: " + token + "\n\nValid for 10 minutes.");
            mailSender.send(message);
        } catch (Throwable t) {
            System.out.println("Reset email failed: " + t.getMessage());
        }
    }


}
