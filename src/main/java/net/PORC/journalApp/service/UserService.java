package net.PORC.journalApp.service;

import net.PORC.journalApp.Exceptionhandler.UserNotFoundException;
import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public void SaveUser(User user){
        userRepository.save(user);
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);

    }
    public User createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("ADMIN"));
        return userRepository.save(user);
    }

}
