package net.PORC.journalApp.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class pageController {
    @GetMapping("/RESET1")
    public String showResetPage() {
        return "reset"; // reset.html in templates
    }

}
