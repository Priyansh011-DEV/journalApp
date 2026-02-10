package net.PORC.journalApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JournalApp {

	public static void main(String[] args) {
		SpringApplication.run(JournalApp.class, args);
	}

}
