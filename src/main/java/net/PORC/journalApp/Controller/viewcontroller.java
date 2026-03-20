package net.PORC.journalApp.Controller;


import net.PORC.journalApp.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

//@RestController

@Controller
public class viewcontroller {

    @Autowired
    private JournalEntryService journalEntryService;

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

    @GetMapping("/JUournal/mood")
    @ResponseBody
    public String getMood(Principal principal) {

        String username = principal.getName();

        return journalEntryService.getTodayMood(username);
    }
}
