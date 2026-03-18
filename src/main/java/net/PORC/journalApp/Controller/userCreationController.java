package net.PORC.journalApp.Controller;


import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/customUser")
public class userCreationController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseBody
    public String registerUser(@RequestBody User user) {
        userService.RegisterUser(user);
        return "user registered successfully";
    }

}
