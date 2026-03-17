package net.PORC.journalApp.service;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


public class testPassword{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void Test() {
        String rawPassword = "12345"; // 👈 SAME password you used while registering
        String encodedPassword = "$2a$10$7GN69kzjhb6HyuzNg6PGoOJw/Cly6ZkJxcrFpb505iDfP3WBJQ5fC"; // 👈 from DB

        boolean match = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("PASSWORD MATCH RESULT: " + match);
    }
}
