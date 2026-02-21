package net.PORC.journalApp.Controller;

import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class adminController {
    @Autowired
    private UserService userService;
    private UserRepository userRepository;


    @GetMapping("/allusers")
    public ResponseEntity<?> getAlluser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
            List<User> all =userService.getAll();
        if(all != null && !all.isEmpty()){
            return ResponseEntity.ok().body(all);

        }

        return ResponseEntity.notFound().build();
    }
    @PostMapping("/createNewAdmin")
    public ResponseEntity <?> CreateNewAdmin(@RequestBody User user){
        userService.createAdmin(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }



}
