package net.PORC.journalApp.Controller;


import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customUser")
public class userCreationController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(User user) {
        userService.RegisterUser(user);
        return "redirect:/login";
    }

}
