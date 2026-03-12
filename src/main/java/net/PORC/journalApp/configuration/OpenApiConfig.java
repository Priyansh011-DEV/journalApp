package net.PORC.journalApp.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI journalApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Journal Management API")
                        .version("1.0")
                        .description("REST API for managing personal journal entries with role-based authentication (Admin/User). Built using Spring Boot, MongoDB Atlas, Docker, and deployed on Render.")
                        .contact(new Contact()
                                .name("Priyansh Kumar")
                                .email("priyanshkumar0077@gmail.com")
                                .url("https://github.com/Priyansh011-DEV"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}