package net.PORC.journalApp.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCHK {
    @GetMapping("health_check")
    public String healthCHK(){
        return " system ok";
    }
}
