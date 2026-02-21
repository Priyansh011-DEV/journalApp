package net.PORC.journalApp.Controller;

import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
