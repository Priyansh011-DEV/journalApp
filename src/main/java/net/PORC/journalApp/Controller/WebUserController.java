package net.PORC.journalApp.Controller;

import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Webuser")
public class WebUserController {
   @Autowired
   private UserService userService;
   @Autowired
   private PasswordEncoder passwordEncoder;
   @Autowired
    UserRepository userRepository;


    @PutMapping
    public ResponseEntity<?> UpdateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User userinDB = userService.FindByUsername(username);

        userinDB.setUsername(user.getUsername());

        if(user.getPassword() != null && !user.getPassword().isEmpty()){
            userinDB.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.UpdateUser(userinDB);

        return ResponseEntity.ok(userinDB);


    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.FindByUsername(username);

        if(user == null){
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteByUsername(username);

        return ResponseEntity.ok("User deleted successfully");
    }
}
