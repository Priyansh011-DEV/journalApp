package net.PORC.journalApp.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController

@Controller
public class viewcontroller {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/journals")
    public String journals() {
        return "journals";
    }
    @GetMapping("/info")
    public String infoPage() {
        return "ai-info"; // refers to ai-info.html
    }
}
