package net.PORC.journalApp.Controller;


import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/CreateUser")
    public ResponseEntity<User> CreateUser(@RequestBody User user){
        userService.RegisterUser(user);
        return ResponseEntity.ok(user);
    }

}
