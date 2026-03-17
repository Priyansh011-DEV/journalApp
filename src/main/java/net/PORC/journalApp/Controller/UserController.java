package net.PORC.journalApp.Controller;


import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService userservice;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PutMapping
    public ResponseEntity<?> UpdateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
       User userinDB = userservice.FindByUsername(username);
        userinDB.setUsername(user.getUsername());
        userinDB.setPassword(user.getPassword());
        userservice.UpdateUser(userinDB);
       return ResponseEntity.ok(userinDB);
    }

    @DeleteMapping
    public ResponseEntity<?> DeleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return ResponseEntity.noContent().build();
    }


}
