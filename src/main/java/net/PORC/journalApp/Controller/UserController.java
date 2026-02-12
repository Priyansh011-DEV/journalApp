package net.PORC.journalApp.Controller;


import net.PORC.journalApp.Repository.JournalEntryRepo;
import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.JournalEntry;
import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.JournalEntryService;
import net.PORC.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService userservice;
    @Autowired
    private UserRepository userRepository;



    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List <User> ALL = userservice.getAll();
            return ResponseEntity.ok().body(ALL);

}
    @PostMapping
    public ResponseEntity<User> CreateUser(@RequestBody User user){
        userservice.NewUser(user);
        return ResponseEntity.ok(user);
    }
    @PutMapping("/{username}")
    public ResponseEntity<?> UpdateUser(@RequestBody User user , @PathVariable String username){
       User userinDB = userservice.FindByUsername(username);
       if(userinDB == null){
           return ResponseEntity.notFound().build();

       }
        userinDB.setUsername(user.getUsername());
        userinDB.setPassword(user.getPassword());
        userservice.SaveEntry(userinDB);
       return ResponseEntity.ok(userinDB);
    }


}
