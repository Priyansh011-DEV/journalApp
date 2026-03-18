package net.PORC.journalApp.service;

import net.PORC.journalApp.Exceptionhandler.UserNotFoundException;
import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;


    public User SaveUser(User user){
        return userRepository.save(user);
    }
    public List<User> getAll(){
        return userRepository.findAll();
    }
    public Optional<User> Find_byID(ObjectId id){
        return userRepository.findById(id);
    }
    public void DeleteEntryByID(ObjectId id){
        userRepository.deleteById(id);
    }

    public User FindByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("user not found  "+ username));
    }
    public void UpdateUser(User user){
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       userRepository.save(user);
    }
    public void RegisterUser(User user){
        // 🔍 NULL CHECK (VERY IMPORTANT)
        if(user.getEmail() == null || user.getEmail().isEmpty()){
            throw new RuntimeException("Email is required");
        }

        // 🔐 CHECK EMAIL UNIQUE
        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new RuntimeException("Email already exists");
        }

        // 🔐 ENCODE PASSWORD
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));

        // 💾 SAVE USER
        userRepository.save(user);

        // 📩 SEND EMAIL (SAFE)
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Welcome 🎉");
            message.setText("Your account has been created successfully!");

           // mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }

    }
    public User createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("ADMIN"));
        return userRepository.save(user);
    }

}
